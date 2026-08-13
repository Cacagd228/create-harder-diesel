#!/usr/bin/env python3
"""Генерация крафтов генераторов и блочного тега mineable/pickaxe."""
import json
import sys
from pathlib import Path

# Категория -> базовое топливо (ведро)
BASE_FUEL_BUCKET = {
    "gasoline": "harderdiesel:low_octane_gasoline_bucket",
    "diesel": "harderdiesel:low_cetane_diesel_bucket",
    "gas": "harderdiesel:propane_bucket",
    "nitro": "harderdiesel:nitromethane_bucket",
}

BASE_ENGINE = {
    "normal": "createdieselgenerators:diesel_engine",
    "modular": "createdieselgenerators:large_diesel_engine",
    "huge": "createdieselgenerators:huge_diesel_engine",
}

SIZES = {
    "normal": "",
    "modular": "large_",
    "huge": "huge_",
}


def main() -> int:
    out = Path("src/main/resources/data/harderdiesel/recipe/crafting")
    out.mkdir(parents=True, exist_ok=True)

    for cat, bucket in BASE_FUEL_BUCKET.items():
        for size, prefix in SIZES.items():
            name = f"{prefix}{cat}_generator"
            base_engine = BASE_ENGINE[size]
            (out / f"{name}.json").write_text(json.dumps({
                "type": "minecraft:crafting_shaped",
                "pattern": [" B ", "BEB", " B "],
                "key": {
                    "B": {"item": bucket},
                    "E": {"item": base_engine},
                },
                "result": {"id": f"harderdiesel:{name}"},
            }, indent=2))

    # mineable/pickaxe
    tag = Path("src/main/resources/data/minecraft/tags/block/mineable/pickaxe.json")
    tag.parent.mkdir(parents=True, exist_ok=True)
    blocks = [f"harderdiesel:{prefix}{cat}_generator" for cat in BASE_FUEL_BUCKET for prefix in SIZES.values()]
    tag.write_text(json.dumps({"replace": False, "values": blocks}, indent=2))

    print("done")
    return 0


if __name__ == "__main__":
    sys.exit(main())
