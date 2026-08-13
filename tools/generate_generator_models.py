#!/usr/bin/env python3
"""Генерация ассетов (модели/блокстейты/предметы) генераторов Harder Diesel.

Копирует модели двигателей из CDG и подставляет перекрашенные текстуры из
assets/harderdiesel/textures/block/<категория>/. Сгенерированные файлы пишутся
в textures/neoforge/assets/harderdiesel/.

Исходники моделей CDG: распакованный jar CDG, assets/createdieselgenerators/models/block.
"""
import json
import sys
from pathlib import Path

CATEGORIES = ["gasoline", "diesel", "gas", "nitro"]

# Замена текстур CDG -> наши (по категории)
TEX_MAP = {
    "createdieselgenerators:block/diesel_engine": "{cat}/diesel_engine",
    "createdieselgenerators:block/diesel_engine_base": "{cat}/diesel_engine_base",
    "createdieselgenerators:block/diesel_engine_big": "{cat}/diesel_engine_big",
    "createdieselgenerators:block/diesel_engine_big_pipe": "{cat}/diesel_engine_big_pipe",
    "createdieselgenerators:block/engine_piston_block": "{cat}/engine_piston_block",
    "createdieselgenerators:block/engine_silencer": "{cat}/engine_silencer",
    "createdieselgenerators:block/engine_turbocharger": "{cat}/engine_turbocharger",
}

# (исходная папка CDG, целевая папка моделей) для каждого типоразмера
SIZES = {
    "normal": ("diesel_engine", "{cat}_generator"),
    "modular": ("modular_diesel_engine", "large_{cat}_generator"),
    "huge": ("huge_diesel_engine", "huge_{cat}_generator"),
}

# Файлы моделей, копируемые из CDG-папки в нашу (исключая jei_*)
COPY_FILES = [
    "block.json",
    "block_pipe.json",
    "block_vertical.json",
    "item.json",
    "piston.json",
    "linkage.json",
    "shaft_connector.json",
    "silencer.json",
    "vertical_silencer.json",
    "turbocharger.json",
    "vertical_turbocharger.json",
]


def tex(cat: str, key: str) -> str:
    return "harderdiesel:block/" + TEX_MAP[key].format(cat=cat)


def substitute(content: str, cat: str) -> str:
    out = content
    for src, tgt in TEX_MAP.items():
        out = out.replace(src, tex(cat, src))
    return out


def copy_models(src_models: Path, dest_models: Path) -> None:
    for size, (src_dir, dest_dir_tpl) in SIZES.items():
        src_folder = src_models / src_dir
        if not src_folder.exists():
            print(f"!! missing source model folder: {src_folder}")
            continue
        for cat in CATEGORIES:
            dest_folder = dest_models / dest_dir_tpl.format(cat=cat)
            dest_folder.mkdir(parents=True, exist_ok=True)
            for f in COPY_FILES:
                src = src_folder / f
                if src.exists():
                    (dest_folder / f).write_text(substitute(src.read_text(), cat))
            pistons = src_folder / "pistons"
            if pistons.exists():
                pdir = dest_folder / "pistons"
                pdir.mkdir(parents=True, exist_ok=True)
                for f in pistons.glob("*.json"):
                    (pdir / f.name).write_text(substitute(f.read_text(), cat))


def copy_blockstates(src_bs: Path, dest_bs: Path) -> None:
    dest_bs.mkdir(parents=True, exist_ok=True)
    # Карта: типоразмер -> (исходник, {подстрока модели -> новая папка модели})
    size_map = {
        "normal": ("diesel_engine", {
            "diesel_engine/block_vertical": "{cat}_generator/block_vertical",
            "diesel_engine/block": "{cat}_generator/block",
        }),
        "modular": ("large_diesel_engine", {
            "modular_diesel_engine/block_pipe": "large_{cat}_generator/block_pipe",
            "modular_diesel_engine/block": "large_{cat}_generator/block",
        }),
        "huge": ("huge_diesel_engine", {
            "huge_diesel_engine/block": "huge_{cat}_generator/block",
        }),
    }
    for cat in CATEGORIES:
        for size, (tmpl, repl) in size_map.items():
            src = src_bs / f"{tmpl}.json"
            if not src.exists():
                print(f"!! missing source blockstate: {src}")
                continue
            data = json.loads(src.read_text())
            model_prefix = "harderdiesel:block/"
            replacements = {old: new.format(cat=cat) for old, new in repl.items()}

            def remap(entry: dict) -> None:
                model = entry.get("model", "")
                for old, new in replacements.items():
                    if old in model:
                        entry["model"] = model_prefix + new
                        return

            for v in data["variants"].values():
                if isinstance(v, dict):
                    remap(v)
                elif isinstance(v, list):
                    for entry in v:
                        remap(entry)
            block_name = {
                "normal": f"{cat}_generator",
                "modular": f"large_{cat}_generator",
                "huge": f"huge_{cat}_generator",
            }[size]
            (dest_bs / f"{block_name}.json").write_text(json.dumps(data, indent=2))


def write_item_models(dest_models: Path) -> None:
    for cat in CATEGORIES:
        items = {
            f"{cat}_generator": f"{cat}_generator/item",
            f"large_{cat}_generator": f"large_{cat}_generator/item",
            f"huge_{cat}_generator": f"huge_{cat}_generator/item",
        }
        for block, parent in items.items():
            item_dir = dest_models.parent / "item"
            item_dir.mkdir(parents=True, exist_ok=True)
            (item_dir / f"{block}.json").write_text(
                json.dumps({"parent": f"harderdiesel:block/{parent}"}, indent=2))


def main() -> int:
    if len(sys.argv) < 2:
        print("usage: generate_generator_models.py <cdg_assets_dir> [out_assets_dir]")
        return 1
    src = Path(sys.argv[1])
    # Исходник можно передать как корень jar (assets) или сразу папку
    # createdieselgenerators внутри него.
    if (src / "createdieselgenerators").exists():
        src = src / "createdieselgenerators"
    out = Path(sys.argv[2]) if len(sys.argv) > 2 else Path("textures/neoforge/assets/harderdiesel")

    copy_models(src / "models/block", out / "models/block")
    copy_blockstates(src / "blockstates", out / "blockstates")
    write_item_models(out / "models/block")
    print("done")
    return 0


if __name__ == "__main__":
    sys.exit(main())
