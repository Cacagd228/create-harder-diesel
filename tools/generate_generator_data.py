#!/usr/bin/env python3
"""Генерация тегов топлива и fuel_type для газов и нитро-топлив.

Газы (пропан/бутан/LPG/wet gas/FCC gas) и нитроэтан/нитропропан/смесь
нитроалканов не имели записей fuel_type — без них генераторы не могли бы их
жечь. Также создаются теги категорий harderdiesel:generator_fuels/<id>,
которые используются фильтром бака генераторов.
"""
import json
import sys
from pathlib import Path

# Категория -> жидкости (id источника)
CATEGORIES = {
    "gasoline": [
        "#c:low_octane_gasoline",
        "#c:high_octane_gasoline",
        "#c:artisan_high_octane_gasoline",
        "#c:kerosene",
    ],
    "diesel": [
        "#c:low_cetane_diesel",
        "#c:medium_cetane_diesel",
        "#c:high_cetane_diesel",
    ],
    "gas": [
        "#c:propane",
        "#c:propane_liquid",
        "#c:butane",
        "#c:butane_liquid",
        "#c:lpg_liquid",
        "#c:petroleum_gas",
        "#c:fcc_gas",
    ],
    "nitro": [
        "#c:nitromethane",
        "#c:nitroethane",
        "#c:nitropropane",
        "#c:mixed_nitroalkanes",
    ],
}

# Жидкости, которым нужен тег c:<name> (источник + flowing)
C_TAGS = [
    "propane",
    "propane_liquid",
    "butane",
    "butane_liquid",
    "lpg_liquid",
    "petroleum_gas",
    "fcc_gas",
    "nitroethane",
    "nitropropane",
    "mixed_nitroalkanes",
]

# (жидкость, pitch, strength нормал/модуль/хьюдж, burn)
FUEL_TYPES = [
    ("propane", 1.05, (6144, 9216, 18432), 0.05),
    ("propane_liquid", 1.05, (6144, 9216, 18432), 0.05),
    ("butane", 1.05, (6144, 9216, 18432), 0.05),
    ("butane_liquid", 1.05, (6144, 9216, 18432), 0.05),
    ("lpg_liquid", 1.05, (7168, 10240, 20480), 0.05),
    ("petroleum_gas", 1.1, (4096, 6144, 12288), 0.05),
    ("fcc_gas", 1.05, (5120, 7680, 15360), 0.05),
    ("nitroethane", 1.2, (11264, 16896, 33792), 0.05),
    ("nitropropane", 1.15, (10240, 15360, 30720), 0.05),
    ("mixed_nitroalkanes", 1.15, (10752, 16128, 32256), 0.05),
]


def tag_file(name, values):
    return {"replace": False, "values": values}


def main() -> int:
    out = Path("src/main/resources/data")
    c_fluid = out / "c/tags/fluid"
    hd_tags = out / "harderdiesel/tags/fluid/generator_fuels"
    hd_fuel = out / "harderdiesel/createdieselgenerators/fuel_type"

    c_fluid.mkdir(parents=True, exist_ok=True)
    hd_tags.mkdir(parents=True, exist_ok=True)
    hd_fuel.mkdir(parents=True, exist_ok=True)

    for name in C_TAGS:
        (c_fluid / f"{name}.json").write_text(
            json.dumps(tag_file(name, [f"harderdiesel:{name}", f"harderdiesel:flowing_{name}"]), indent=2))

    for cat, values in CATEGORIES.items():
        (hd_tags / f"{cat}.json").write_text(
            json.dumps(tag_file(cat, values), indent=2))

    for name, pitch, (n, m, h), burn in FUEL_TYPES:
        (hd_fuel / f"{name}.json").write_text(json.dumps({
            "fluid": f"#c:{name}",
            "sound_pitch": pitch,
            "normal": {"speed": 96.0, "strength": float(n), "burn_rate": burn},
            "modular": {"speed": 96.0, "strength": float(m), "burn_rate": burn},
            "huge": {"speed": 224.0, "strength": float(h), "burn_rate": burn},
            "burner_multiplier": 1.0,
        }, indent=2))

    print("done")
    return 0


if __name__ == "__main__":
    sys.exit(main())
