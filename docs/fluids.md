# Жидкости

## Базовые статы (density / viscosity)

| Жидкость | Плотность | Вязкость |
|---|---|---|
| naphtha | 650 | 600 |
| kerosene | 800 | 1200 |
| heavy_gas_oil | 1400 | 4000 |
| tar | 1100 | 8000 |
| low_octane_gasoline | 740 | 900 |
| high_octane_gasoline | 760 | 800 |
| artisan_high_octane_gasoline | 780 | 750 |
| aviation_fuel | 790 | 1000 |
| nitromethane | 1120 | 700 |
| propane | 500 | 350 |
| mixed_nitroalkanes | 1050 | 650 |
| nitroethane | 1050 | 680 |
| nitropropane | 990 | 620 |
| low_cetane_diesel | 830 | 2400 |
| medium_cetane_diesel | 850 | 2600 |
| high_cetane_diesel | 870 | 2800 |
| mazut | 950 | 5000 |
| butane | 560 | 250 |
| butane_liquid | 600 | 300 |
| lpg_liquid | 620 | 320 |
| petroleum_gas | 580 | 280 |
| propane_liquid | 510 | 400 |
| vacuum_gas_oil | 960 | 3500 |
| vacuum_residue | 1020 | 9000 |
| cracked_naphtha | 730 | 700 |
| light_cycle_oil | 880 | 2000 |
| fcc_gas | 550 | 260 |
| alkylate | 700 | 600 |
| glycerol | 1260 | 9000 |
| nitroglycerin | 1590 | 1300 |
| reformate | 770 | 700 |
| benzene | 880 | 650 |
| toluene | 870 | 590 |
| xylene | 860 | 650 |
| light_sweet_crude | 700 | 800 |
| light_sour_crude | 710 | 850 |
| medium_sweet_crude | 830 | 1500 |
| medium_sour_crude | 840 | 1600 |
| heavy_sweet_crude | 950 | 4000 |
| heavy_sour_crude | 960 | 4200 |
| molten_sulfur | 1800 | 6000 |
| sour_naphtha | 665 | 650 |
| sour_kerosene | 820 | 1300 |
| ethylene | 430 | 230 |
| propylene | 490 | 260 |
| butadiene | 550 | 300 |
| sulfuric_acid | 1830 | 2600 |

## Топливные статы (fuel_type)

`speed` у всех жидкостей 96/96/224, кроме `tar` — 64/64/160. `strength` указан
для трёх размеров генераторов (обычный / модульный / большой).

| Жидкость | normal | modular | huge | burn_rate | burner_multiplier |
|---|---|---|---|---|---|
| kerosene | 5120 | 7168 | 14336 | 0.05 | 0.55 |
| low_octane_gasoline | 4096 | 6144 | 12288 | 0.05 | 1.00 |
| high_octane_gasoline | 7168 | 10240 | 20480 | 0.045 | 1.10 |
| artisan_high_octane_gasoline | 9216 | 13824 | 27648 | 0.04 | 1.20 |
| aviation_fuel | 8294 | 12442 | 24883 | 0.02 | 1.00 |
| nitromethane | 12288 | 18432 | 36864 | 0.05 | 1.50 |
| nitroethane | 11264 | 16896 | 33792 | 0.05 | 1.00 |
| nitropropane | 10240 | 15360 | 30720 | 0.05 | 1.00 |
| mixed_nitroalkanes | 10752 | 16128 | 32256 | 0.05 | 1.00 |
| low_cetane_diesel | 5120 | 7168 | 14336 | 0.05 | 0.50 |
| medium_cetane_diesel | 6144 | 9216 | 18432 | 0.045 | 0.55 |
| high_cetane_diesel | 8192 | 12288 | 24576 | 0.04 | 0.60 |
| propane | 6144 | 9216 | 18432 | 0.05 | 1.00 |
| propane_liquid | 6144 | 9216 | 18432 | 0.05 | 1.00 |
| butane | 6144 | 9216 | 18432 | 0.05 | 1.00 |
| butane_liquid | 6144 | 9216 | 18432 | 0.05 | 1.00 |
| lpg_liquid | 7168 | 10240 | 20480 | 0.05 | 1.00 |
| petroleum_gas | 4096 | 6144 | 12288 | 0.05 | 1.00 |
| fcc_gas | 5120 | 7680 | 15360 | 0.05 | 1.00 |
| tar | 1024 | 1280 | 2048 | 0.05 | 0.30 |

## Теги генераторов (`generator_fuels`)

- **`gasoline`**: `low_octane_gasoline`, `high_octane_gasoline`,
  `artisan_high_octane_gasoline`, `kerosene`, `aviation_fuel`
- **`diesel`**: `low_cetane_diesel`, `medium_cetane_diesel`, `high_cetane_diesel`
- **`gas`**: `propane`, `propane_liquid`, `butane`, `butane_liquid`, `lpg_liquid`,
  `petroleum_gas`, `fcc_gas`
- **`nitro`**: `nitromethane`, `nitroethane`, `nitropropane`, `mixed_nitroalkanes`

## Сводка по каждой жидкости

Статусы: ✅ — в рабочем дереве; ⚠️ — только топливо / вход; ❌ — сирота.

| Жидкость | Производится из | Потребляется в | Статус |
|---|---|---|---|
| naphtha | перегонка сырья/тяж.газойля/мазута, очистка | перегонка нафты | ✅ |
| kerosene | перегонка сырья, очистка | авиатопливо | ✅ |
| heavy_gas_oil | перегонка сырья | перегонка тяж.газойля | ✅ |
| tar | перегонка мазута, крекинг | асфальт, коксование | ✅ |
| low_octane_gasoline | перегонка нафты | высокоокт./кустарный бензин | ✅ |
| high_octane_gasoline | mixing | — | ✅ |
| artisan_high_octane_gasoline | mixing | — | ✅ |
| aviation_fuel | mixing | — | ✅ |
| nitromethane | перегонка/сепарация нитроалканов | кустарный бензин | ✅ |
| propane | перегонка, сепарация нефт.газа | нитроалканы, сжижение, LPG | ✅ |
| mixed_nitroalkanes | mixing | перегонка, сепарация | ✅ |
| nitroethane | перегонка/сепарация нитроалканов | авиатопливо, высокоцет.дизель | ✅ |
| nitropropane | перегонка/сепарация нитроалканов | высокоцет.дизель | ✅ |
| low_cetane_diesel | перегонка нафты | среднецет.дизель | ✅ |
| medium_cetane_diesel | mixing | высокоцет.дизель | ✅ |
| high_cetane_diesel | mixing | — | ✅ |
| mazut | перегонка тяж.газойля | перегонка мазута | ✅ |
| butane | сепарация нефт.газа | сжижение, LPG | ✅ |
| butane_liquid | сжижение бутана | — | ⚠️ только топливо |
| lpg_liquid | LPG из газов | крекинг LPG | ✅ |
| petroleum_gas | перегонка лёгких сортов | сепарация | ✅ |
| propane_liquid | сжижение пропана | — | ⚠️ только топливо |
| vacuum_gas_oil | крекинг вак.остатка | FCC-крекинг | ✅ |
| vacuum_residue | перегонка тяжёлых сортов | крекинг | ✅ |
| cracked_naphtha | FCC-крекинг | риформинг | ✅ |
| light_cycle_oil | FCC-крекинг | высокоцет.дизель (LCO) | ✅ |
| fcc_gas | FCC-крекинг | алкилат | ✅ |
| alkylate | алкилирование | — | ✅ |
| glycerol | mixing | нитроглицерин | ✅ |
| nitroglycerin | mixing | — | ✅ |
| reformate | риформинг | BTX-сепарация | ✅ |
| benzene / toluene / xylene | BTX-сепарация | — | ✅ |
| light_* / medium_* / heavy_* crude | **нет** (биомы не подключены) | перегонка | ⚠️ вход |
| molten_sulfur | очистка сернистых фракций | серная кислота, каучук | ✅ |
| sour_naphtha | перегонка кислых сортов | очистка | ✅ |
| sour_kerosene | перегонка кислых сортов | очистка | ✅ |
| ethylene / propylene / butadiene | крекинг LPG | полимеры | ✅ |
| sulfuric_acid | mixing | нитроалканы | ✅ |

## Внешние жидкости (входы)

- `c:crude_oil` — сырая нефть из CDG;
- `minecraft:water`;
- `createdieselgenerators:ethanol` — этанол (CDG);
- `createbb:hydrogen` — водород (Create Broken Bad);
- `powergrid:acid` — кислота (Power Grid, для серной кислоты).
