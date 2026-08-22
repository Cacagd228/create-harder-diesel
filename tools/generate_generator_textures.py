#!/usr/bin/env python3
"""Генерация перекрашенных текстур генераторов для Create: Harder Diesel.

Берёт исходные текстуры двигателей из мода Create Diesel Generators (CDG) и
перекрашивает их в цвет семейства топлива: hue каждого пикселя заменяется на
целевой оттенок категории (насыщенность и яркость сохраняются — металлический
объём не теряется). Серая/белая гамма остаётся почти неизменной.

Исходники текстур CDG должны лежать в папке, переданной первым аргументом
(обычно это распакованный jar CDG: assets/createdieselgenerators/textures/block).
"""
import colorsys
import sys
from pathlib import Path

from PIL import Image

# Категория -> целевой оттенок в градусах
CATEGORIES = {
    "gasoline": 30,   # оранжево-янтарный (бензин)
    "diesel": 240,    # стально-синий (дизель)
    "gas": 185,       # циан (газ)
    "nitro": 285,     # пурпурно-малиновый (нитро)
}

SOURCES = [
    "diesel_engine",
    "diesel_engine_base",
    "diesel_engine_big",
    "diesel_engine_big_connected",
    "diesel_engine_big_pipe",
    "engine_piston_block",
    "engine_silencer",
    "engine_turbocharger",
]


def recolor(im: Image.Image, target_hue: float) -> Image.Image:
    rgba = im.convert("RGBA")
    out = Image.new("RGBA", rgba.size)
    src = rgba.load()
    dst = out.load()
    target = target_hue / 360.0
    for y in range(rgba.height):
        for x in range(rgba.width):
            r, g, b, a = src[x, y]
            if a == 0:
                dst[x, y] = (r, g, b, 0)
                continue
            h, s, v = colorsys.rgb_to_hsv(r / 255.0, g / 255.0, b / 255.0)
            nr, ng, nb = colorsys.hsv_to_rgb(target, s, v)
            dst[x, y] = (int(nr * 255), int(ng * 255), int(nb * 255), a)
    return out


def main() -> int:
    if len(sys.argv) < 2:
        print("usage: generate_generator_textures.py <cdg_textures_dir> [out_dir]")
        return 1
    src_dir = Path(sys.argv[1])
    out_dir = Path(sys.argv[2]) if len(sys.argv) > 2 else Path(
        "src/main/resources/assets/harderdiesel/textures/block")

    for cat, hue in CATEGORIES.items():
        cat_dir = out_dir / cat
        cat_dir.mkdir(parents=True, exist_ok=True)
        for name in SOURCES:
            src = src_dir / f"{name}.png"
            if not src.exists():
                print(f"!! missing source texture: {src}")
                continue
            out = recolor(Image.open(src), hue)
            out.save(cat_dir / f"{name}.png")
            print(f"wrote {cat_dir / name}.png")
    return 0


if __name__ == "__main__":
    sys.exit(main())
