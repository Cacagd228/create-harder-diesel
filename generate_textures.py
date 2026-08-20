#!/usr/bin/env python3
"""
Генератор текстур жидкостей для Create: Harder Diesel.

Реализует алгоритм, описанный в agent.md:
  - Still: базовый цвет + синусоидальная рябь
      wave = sin(x/16*2pi*freq + y*0.5) + sin(y/16*2pi*freq*0.7 + x*0.3)
      shade = wave * ripple_strength
      + случайный пиксельный шум
  - Flow: та же идея, но волна идёт по диагонали (x+y) — направленные полосы.
  - Модификаторы:
      add_bubbles   — газообразные жидкости
      add_blotches  — «грязные» / неочищенные (сырая нефть)
      add_sparkle   — экзотика (яркие блики)

Использование:
    python3 generate_textures.py            # сгенерировать всё из конфига ниже
    python3 generate_textures.py light_sweet_crude heavy_sour_crude
"""

import os
import sys

import numpy as np
from PIL import Image

OUT_DIR = os.path.join("textures", "neoforge", "assets", "harderdiesel", "textures", "block")
SIZE = 16

# name -> {base: (r,g,b), alpha, ripple, freq, modifiers: [...]}
FLUIDS = {
    "light_sweet_crude": dict(base=(212, 172, 95), alpha=150, ripple=20, freq=1.6, modifiers=["blotches"]),
    "light_sour_crude": dict(base=(204, 176, 74), alpha=155, ripple=20, freq=1.6, modifiers=["blotches"]),
    "medium_sweet_crude": dict(base=(142, 97, 50), alpha=205, ripple=26, freq=1.3, modifiers=["blotches"]),
    "medium_sour_crude": dict(base=(132, 102, 46), alpha=210, ripple=26, freq=1.3, modifiers=["blotches"]),
    "heavy_sweet_crude": dict(base=(46, 39, 31), alpha=255, ripple=30, freq=1.0, modifiers=["blotches"]),
    "heavy_sour_crude": dict(base=(41, 43, 30), alpha=255, ripple=30, freq=1.0, modifiers=["blotches"]),
    "molten_sulfur": dict(base=(255, 214, 40), alpha=255, ripple=24, freq=1.4, modifiers=["sparkle"]),
    "sour_naphtha": dict(base=(205, 205, 105), alpha=170, ripple=20, freq=1.6, modifiers=["blotches"]),
    "sour_kerosene": dict(base=(224, 196, 96), alpha=185, ripple=22, freq=1.4, modifiers=["blotches"]),
    "ethylene": dict(base=(170, 220, 170), alpha=90, ripple=16, freq=1.8, modifiers=["bubbles"]),
    "propylene": dict(base=(160, 200, 230), alpha=95, ripple=16, freq=1.8, modifiers=["bubbles"]),
    "butadiene": dict(base=(235, 210, 130), alpha=100, ripple=17, freq=1.7, modifiers=["bubbles"]),
    "aviation_fuel": dict(base=(150, 190, 210), alpha=180, ripple=20, freq=1.5, modifiers=[]),
    "sulfuric_acid": dict(base=(205, 220, 120), alpha=200, ripple=18, freq=1.5, modifiers=[]),
}


def base_wave(flow, freq, rng):
    x = np.arange(SIZE, dtype=np.float64)[:, None]
    y = np.arange(SIZE, dtype=np.float64)[None, :]
    if flow:
        d = (x + y) / SIZE
        wave = np.sin(d * 2 * np.pi * freq) + np.sin(d * 2 * np.pi * freq * 0.7 + x * 0.3)
    else:
        wave = np.sin(x / SIZE * 2 * np.pi * freq + y * 0.5) + np.sin(y / SIZE * 2 * np.pi * freq * 0.7 + x * 0.3)
    return wave


def add_bubbles(arr, rng):
    r, g, b = arr[..., 0], arr[..., 1], arr[..., 2]
    for _ in range(rng.integers(3, 6)):
        cx, cy = rng.uniform(0, SIZE), rng.uniform(0, SIZE)
        rad = rng.uniform(2.0, 5.0)
        yy, xx = np.mgrid[0:SIZE, 0:SIZE]
        d = np.sqrt((xx - cx) ** 2 + (yy - cy) ** 2)
        mask = np.clip(1.0 - d / rad, 0, 1)
        bright = rng.integers(25, 70)
        r += mask * bright
        g += mask * bright
        b += mask * bright
    arr[..., 0], arr[..., 1], arr[..., 2] = r, g, b


def add_blotches(arr, rng):
    r, g, b = arr[..., 0], arr[..., 1], arr[..., 2]
    for _ in range(rng.integers(3, 6)):
        cx, cy = rng.uniform(0, SIZE), rng.uniform(0, SIZE)
        rad = rng.uniform(1.5, 4.5)
        yy, xx = np.mgrid[0:SIZE, 0:SIZE]
        d = np.sqrt((xx - cx) ** 2 + (yy - cy) ** 2)
        mask = np.clip(1.0 - d / rad, 0, 1)
        delta = rng.integers(-45, -8)
        r = np.clip(r + mask * delta, 0, 255)
        g = np.clip(g + mask * delta, 0, 255)
        b = np.clip(b + mask * delta * 0.7, 0, 255)
    arr[..., 0], arr[..., 1], arr[..., 2] = r, g, b


def add_sparkle(arr, rng):
    r, g, b = arr[..., 0], arr[..., 1], arr[..., 2]
    for _ in range(rng.integers(6, 12)):
        x, y = rng.integers(0, SIZE), rng.integers(0, SIZE)
        amount = rng.integers(50, 130)
        r[y, x] = np.clip(r[y, x] + amount, 0, 255)
        g[y, x] = np.clip(g[y, x] + amount, 0, 255)
        b[y, x] = np.clip(b[y, x] + amount, 0, 255)


def build(flow, spec, seed):
    rng = np.random.default_rng(seed)
    base = np.array(spec["base"], dtype=np.float64)
    alpha = spec["alpha"]
    ripple = spec["ripple"]
    freq = spec["freq"]

    arr = np.repeat(base[None, None, :], SIZE * SIZE, axis=0).reshape(SIZE, SIZE, 3).copy()

    wave = base_wave(flow, freq, rng)
    shade = wave * ripple
    arr = arr + shade[..., None]

    noise = rng.normal(0, 7, size=(SIZE, SIZE, 3))
    arr = arr + noise

    for mod in spec.get("modifiers", []):
        if mod == "bubbles":
            add_bubbles(arr, rng)
        elif mod == "blotches":
            add_blotches(arr, rng)
        elif mod == "sparkle":
            add_sparkle(arr, rng)

    arr = np.clip(arr, 0, 255).astype(np.uint8)
    rgba = np.dstack([arr, np.full((SIZE, SIZE), alpha, dtype=np.uint8)])
    return Image.fromarray(rgba, "RGBA")


def generate(name):
    if name not in FLUIDS:
        raise SystemExit(f"Unknown fluid: {name}")
    os.makedirs(OUT_DIR, exist_ok=True)
    spec = FLUIDS[name]
    seed = sum(ord(c) for c in name)
    for suffix, flow in (("still", False), ("flow", True)):
        img = build(flow, spec, seed + (1 if flow else 0))
        path = os.path.join(OUT_DIR, f"{name}_{suffix}.png")
        img.save(path)
        print(f"  {path}")


def main():
    targets = sys.argv[1:] or list(FLUIDS.keys())
    for name in targets:
        generate(name)


if __name__ == "__main__":
    main()
