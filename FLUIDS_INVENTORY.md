# Create: Harder Diesel — полная инвентаризация жидкостей, крафтов и производственных блоков

> Собрано автоматически по состоянию кода и ресурсов (ветка `main`).
> Все объёмы жидкостей — в миллилитрах (mb). Используется как база для
> переписывания дерева крафтов с нуля.

---

## 1. Жидкости: базовые статы (density / viscosity)

Источник: `src/main/java/com/harderdiesel/ModFluidTypes.java`.
Остальные свойства у всех жидкостей одинаковые (звуки ведра, без света).

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

---

## 2. Топливные статы (fuel_type)

Источник: `src/main/resources/data/harderdiesel/createdieselgenerators/fuel_type/`.
`speed` у всех жидкостей 96/96/224, кроме tar — 64/64/160.
`strength` указан для трёх размеров генераторов (обычный / модульный / большой).

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

**Теги генераторов** (`data/harderdiesel/tags/fluid/generator_fuels/`):

- `gasoline`: `#c:low_octane_gasoline`, `#c:high_octane_gasoline`,
  `#c:artisan_high_octane_gasoline`, `#c:kerosene`, `#c:aviation_fuel`
- `diesel`: `#c:low_cetane_diesel`, `#c:medium_cetane_diesel`, `#c:high_cetane_diesel`
- `gas`: `#c:propane`, `#c:propane_liquid`, `#c:butane`, `#c:butane_liquid`,
  `#c:lpg_liquid`, `#c:petroleum_gas`, `#c:fcc_gas`
- `nitro`: `#c:nitromethane`, `#c:nitroethane`, `#c:nitropropane`,
  `#c:mixed_nitroalkanes`

**Тег `c:fuel`** (`data/c/tags/fluid/fuel.json`): kerosene, tar, low_octane_gasoline,
low_cetane_diesel, medium_cetane_diesel, high_octane_gasoline, high_cetane_diesel,
artisan_high_octane_gasoline, aviation_fuel, nitromethane (вместе с `flowing_*`).
Внимание: kerosene встречается в файле дважды (дубликат, не влияет на работу).

---

## 3. Все крафты (JSON-рецепты)

### 3.1 Перегонка / Ректификация — `data/createdieselgenerators/recipe/distillation/` (21)

| Файл | Вход (100 mb) | Тепло | Время | Выход |
|---|---|---|---|---|
| crude_oil | `#c:crude_oil` | heated | 60 | нафта 40 + керосин 20 + тяж.газойль 25 + пропан 10 |
| superheated_crude_oil | `#c:crude_oil` | superheated | 40 | нафта 60 + керосин 40 + тяж.газойль 40 + пропан 15 |
| light_sweet_crude | лёгкая сладкая нефть | heated | 60 | нафта 55 + керосин 15 + тяж.газойль 10 + нефт.газ 15 |
| superheated_light_sweet_crude | лёгкая сладкая нефть | superheated | 40 | нафта 80 + керосин 22 + тяж.газойль 15 + нефт.газ 22 |
| light_sour_crude | лёгкая кислая нефть | heated | 60 | серн.нафта 45 + серн.керосин 15 + тяж.газойль 10 + нефт.газ 12 |
| superheated_light_sour_crude | лёгкая кислая нефть | superheated | 40 | серн.нафта 65 + серн.керосин 22 + тяж.газойль 15 + нефт.газ 17 |
| medium_sweet_crude | средняя сладкая нефть | heated | 60 | нафта 32 + керосин 20 + тяж.газойль 32 |
| superheated_medium_sweet_crude | средняя сладкая нефть | superheated | 40 | нафта 46 + керосин 29 + тяж.газойль 46 |
| medium_sour_crude | средняя кислая нефть | heated | 60 | серн.нафта 27 + серн.керосин 18 + тяж.газойль 30 |
| superheated_medium_sour_crude | средняя кислая нефть | superheated | 40 | серн.нафта 39 + серн.керосин 26 + тяж.газойль 44 |
| heavy_sweet_crude | тяжёлая сладкая нефть | heated | 60 | нафта 17 + керосин 15 + тяж.газойль 47 + вак.остаток 10 |
| superheated_heavy_sweet_crude | тяжёлая сладкая нефть | superheated | 40 | нафта 25 + керосин 22 + тяж.газойль 68 + вак.остаток 15 |
| heavy_sour_crude | тяжёлая кислая нефть | heated | 60 | серн.нафта 5 + серн.керосин 5 + тяж.газойль 55 + вак.остаток 5 |
| superheated_heavy_sour_crude | тяжёлая кислая нефть | superheated | 40 | серн.нафта 7 + серн.керосин 7 + тяж.газойль 80 + вак.остаток 7 |
| heavy_gas_oil | тяж.газойль | heated | 120 | мазут 70 + нафта 10 |
| superheated_heavy_gas_oil | тяж.газойль | superheated | 80 | мазут 80 + нафта 15 |
| mazut | мазут | heated | 120 | гудрон 70 + нафта 5 |
| superheated_mazut | мазут | superheated | 80 | гудрон 80 + нафта 8 |
| naphtha | нафта | heated | 60 | низкоокт.бензин 60 + низкоцет.дизель 30 |
| superheated_naphtha | нафта | superheated | 40 | низкоокт.бензин 70 + низкоцет.дизель 40 |
| superheated_mixed_nitroalkanes | нитроалканы | superheated | 100 | нитрометан 30 + нитроэтан 40 + нитропропан 30 |

### 3.2 Крекинг — `data/harderdiesel/recipe/cracking/` (4)

| Файл | Вход | Тепло | Время | Выход |
|---|---|---|---|---|
| vacuum_residue | вакуумный остаток 100 | superheated | 120 | вак.газойль 70 + гудрон 15 |
| lpg_liquid | LPG-жидкость 100 | superheated | 180 | этилен 35 + пропилен 35 + бутадиен 25 |
| vacuum_gas_oil (FCC) | вак.газойль 100 | superheated | 100 | крек.нафта 40 + лёгк.цикл.газойль 30 + FCC-газ 20 |
| cracked_naphtha (риформинг) | крек.нафта 100 | superheated | 80 | риформат 60 |

### 3.3 Сепарация — `data/harderdiesel/recipe/separating/` (3)

| Файл | Вход | Тепло | Время | Выход |
|---|---|---|---|---|
| petroleum_gas | нефтяной газ 100 | нет | 60 | пропан 50 + бутан 50 |
| mixed_nitroalkanes | нитроалканы 100 | нет | 100 | нитрометан 30 + нитроэтан 40 + нитропропан 30 |
| reformate (BTX) | риформат 100 | нет | 80 | бензол 30 + толуол 40 + ксилол 30 |

### 3.4 Смешивание — `data/createdieselgenerators/recipe/mixing/` (24)

| Файл | Вход | Тепло* | Выход |
|---|---|---|---|
| high_octane_gasoline | низкоокт.бензин 100 + этанол 50 | нет | высокоокт.бензин 100 |
| artisan_high_octane_gasoline | низкоокт.бензин 100 + нитрометан 10 | нет | кустарный высокоокт.бензин 100 |
| aviation_fuel | керосин 100 + нитроэтан 25 | нет | авиатопливо 100 |
| medium_cetane_diesel **[createbb]** | низкоцет.дизель 100 + водород 10 | heated | среднецет.дизель 100 |
| high_cetane_diesel_nitroethane | среднецет.дизель 100 + нитроэтан 20 | heated | высокоцет.дизель 100 |
| high_cetane_diesel_nitropropane | среднецет.дизель 100 + нитропропан 10 | heated | высокоцет.дизель 100 |
| high_cetane_diesel_lco | среднецет.дизель 100 + лёгк.цикл.газойль 20 | heated | высокоцет.дизель 100 |
| mixed_nitroalkanes **[powergrid]** | серная кислота 25 + пропан 25 | heated | нитроалканы 35 |
| sulfuric_acid **[powergrid]** | распл.сера 25 + кислота 25 | heated | серная кислота 35 |
| sour_naphtha_to_naphtha **[createbb]** | серн.нафта 100 + водород 10 | heated | нафта 95 + сера 5 |
| sour_kerosene_to_kerosene **[createbb]** | серн.керосин 100 + водород 10 | heated | керосин 95 + сера 5 |
| liquefy_propane | пропан 100 + лёд ×1 | нет | сжиженный пропан 10 |
| liquefy_butane | бутан 100 + лёд ×1 | нет | сжиженный бутан 10 |
| lpg_liquid_from_gases | пропан 60 + бутан 40 + лёд ×1 | нет | LPG-жидкость 10 |
| alkylate | FCC-газ 100 + серная кислота 50 | heated | алкилат 100 |
| glycerol | пропилен 100 + вода 50 | heated | глицерин 60 |
| nitroglycerin **[powergrid]** | глицерин 100 + серная кислота 50 + кислота 50 | heated | нитроглицерин 100 |
| polyethylene | этилен 250 + андезитовый сплав ×1 | heated | полиэтилен ×4 |
| polypropylene | пропилен 250 + андезитовый сплав ×2 | heated | полипропилен ×4 |
| synthetic_rubber | бутадиен 250 + распл.сера 50 | heated | каучук ×4 |
| coking | гудрон 100 | heated | кокс ×2 + нефт.газ 20 |
| asphalt_block | гравий×2 + песок×2 + гудрон 100 | heated | асфальт×4 |

\* **Внимание (баг):** в Create 6.0.10 `create:mixing` парсится через кодек с
ключом `heat_requirement` (snake_case). Часть рецептов всё ещё использует
`heatRequirement` (camelCase) — он игнорируется. Новые рецепты пишутся через
`heat_requirement`, чтобы нагрев реально требовался.

**Условные рецепты:** `medium_cetane_diesel`, `sour_naphtha_to_naphtha`,
`sour_kerosene_to_kerosene` — только при моде `createbb` (водород);
`mixed_nitroalkanes`, `sulfuric_acid` — только при моде `powergrid` (кислота).

---

## 4. Сводка по каждой жидкости (производство / потребление / статус)

Статусы:
- ✅ — включена в работающее дерево;
- ⚠️ — только топливо / не производится / не потребляется;
- ❌ — сирота (нет ни крафта, ни применения).

| Жидкость | Производится из | Потребляется в | fuel_type | Теги генер. | Статус |
|---|---|---|---|---|---|
| naphtha | перегонка сырья/тяж.газойля/мазута, очистка серн.нафты | перегонка нафты | — | — | ✅ |
| kerosene | перегонка сырья, очистка серн.керосина | авиатопливо | ✅ | gasoline, c:fuel | ✅ |
| heavy_gas_oil | перегонка сырья | перегонка тяж.газойля | — | — | ✅ |
| tar | перегонка мазута, крекинг вак.остатка | асфальт, коксование | ✅ | c:fuel | ✅ |
| low_octane_gasoline | перегонка нафты | высокоокт./кустарный бензин | ✅ | gasoline, c:fuel | ✅ |
| high_octane_gasoline | mixing | — | ✅ | gasoline, c:fuel | ✅ |
| artisan_high_octane_gasoline | mixing | — | ✅ | gasoline, c:fuel | ✅ |
| aviation_fuel | mixing (керосин+нитроэтан) | — | ✅ | gasoline, c:fuel | ✅ |
| nitromethane | перегонка нитроалканов, сепарация | кустарный бензин | ✅ | nitro, c:fuel | ✅ |
| propane | перегонка сырца (CDG), сепарация нефт.газа | нитроалканы, сжижение, LPG | ✅ | gas | ✅ |
| mixed_nitroalkanes | mixing (серная кислота+пропан) | перегонка, сепарация | ✅ | nitro | ✅ |
| nitroethane | перегонка/сепарация нитроалканов | авиатопливо, высокоцет.дизель | ✅ | nitro | ✅ |
| nitropropane | перегонка/сепарация нитроалканов | высокоцет.дизель | ✅ | nitro | ✅ |
| low_cetane_diesel | перегонка нафты | среднецет.дизель (createbb) | ✅ | diesel, c:fuel | ✅ |
| medium_cetane_diesel | mixing | высокоцет.дизель ×2 | ✅ | diesel, c:fuel | ✅ |
| high_cetane_diesel | mixing ×2 | — | ✅ | diesel, c:fuel | ✅ |
| mazut | перегонка тяж.газойля | перегонка мазута | — | — | ✅ |
| butane | сепарация нефт.газа | сжижение, LPG | ✅ | gas | ✅ |
| butane_liquid | сжижение бутана | — | ✅ | gas | ⚠️ только топливо |

| lpg_liquid | LPG из газов (mixing) | крекинг LPG | ✅ | gas | ✅ |
| petroleum_gas | перегонка лёгких сортов | сепарация | ✅ | gas | ✅ |
| propane_liquid | сжижение пропана | — | ✅ | gas | ⚠️ только топливо |
| vacuum_gas_oil | крекинг вак.остатка | FCC-крекинг | ❌ | ❌ | ✅ |
| vacuum_residue | перегонка тяжёлых сортов | крекинг | ❌ | ❌ | ✅ |
| cracked_naphtha | FCC-крекинг | риформинг | ❌ | ❌ | ✅ |
| light_cycle_oil | FCC-крекинг | высокоцет.дизель (LCO) | ❌ | ❌ | ✅ |
| fcc_gas | FCC-крекинг | алкилат | ✅ | gas | ✅ |
| alkylate | алкилирование (FCC-газ+кислота) | — | ❌ | ❌ | ✅ |
| glycerol | mixing (пропилен+вода) | нитроглицерин | ❌ | ❌ | ✅ |
| nitroglycerin | mixing (глицерин+кислоты) | — | ❌ | ❌ | ✅ |
| reformate | риформинг | BTX-сепарация | ❌ | ❌ | ✅ |
| benzene | BTX-сепарация | — | ❌ | ❌ | ✅ |
| toluene | BTX-сепарация | — | ❌ | ❌ | ✅ |
| xylene | BTX-сепарация | — | ❌ | ❌ | ✅ |
| light_sweet_crude | **нет** (биомы не подключены) | перегонка | ❌ | ❌ | ⚠️ вход, не производится |
| light_sour_crude | **нет** | перегонка | ❌ | ❌ | ⚠️ вход, не производится |
| medium_sweet_crude | **нет** | перегонка | ❌ | ❌ | ⚠️ вход, не производится |
| medium_sour_crude | **нет** | перегонка | ❌ | ❌ | ⚠️ вход, не производится |
| heavy_sweet_crude | **нет** | перегонка | ❌ | ❌ | ⚠️ вход, не производится |
| heavy_sour_crude | **нет** | перегонка | ❌ | ❌ | ⚠️ вход, не производится |
| molten_sulfur | очистка серн.нафты/керосина | серная кислота, каучук | ❌ | ❌ | ✅ |
| sour_naphtha | перегонка кислых сортов | очистка | ❌ | ❌ | ✅ |
| sour_kerosene | перегонка кислых сортов | очистка | ❌ | ❌ | ✅ |
| ethylene | крекинг LPG | полиэтилен | ❌ | ❌ | ✅ |
| propylene | крекинг LPG | полипропилен | ❌ | ❌ | ✅ |
| butadiene | крекинг LPG | каучук | ❌ | ❌ | ✅ |
| sulfuric_acid | mixing (сера+кислота) | нитроалканы | ❌ | ❌ | ✅ |

**Внешние жидкости, используемые как вход:**
- `c:crude_oil` — сырая нефть из CDG (тег `c:crude_oil`);
- `minecraft:water`;
- `createdieselgenerators:ethanol` — этанол (CDG);
- `createbb:hydrogen` — водород (Create Broken Bad);
- `powergrid:acid` — кислота (Power Grid, для серной кислоты).

**Предметы:** `coke_coal` — производится коксованием гудрона (mixing),
применения пока нет. `polyethylene`, `polypropylene`, `synthetic_rubber` —
производятся полимеризацией, применения пока нет.

---

## 5. Производственные блоки

### 5.1 Ректификационная колонна (Distillation Tower) — мод CDG

- Базовый блок: нефтяная бочка CDG (`createdieselgenerators:oil_barrel`);
  активируется **контроллером дистилляции CDG** (не наш).
- Мультиблок из бочек, объединённых `ConnectivityHandler`.
- **1 жидкий вход** (в нижний блок), **до 6 выходов** — по слою на каждый продукт.
- Требует нагрев снизу; рецепты бывают `heated` / `superheated`.
- Управляется рецептами типа `createdieselgenerators:distillation`.
- Рецепты колонны лежат в `data/createdieselgenerators/recipe/distillation/`.

### 5.2 Крекинг-реактор (Cracking Reactor) — наш мультиблок

- Базовый блок: **оцинкованный бак реактора** `harderdiesel:galvanized_reactor_tank`.
- Активируется предметом `harderdiesel:cracking_controller`.
- Минимальная высота: `ModConfig.CRACKING_MIN_HEIGHT`.
- **2 жидких входа** (вход 1 — нижний блок, вход 2 — второй снизу),
  **до 6 жидких выходов** (только жидкости).
- Тепло снизу; `heat_requirement` обязателен.
- Рецепты типа `harderdiesel:cracking`
  (файлы в `data/harderdiesel/recipe/cracking/`): `vacuum_residue`, `lpg_liquid`.

### 5.3 Сепаратор (Separator) — наш мультиблок

- Базовый блок: **износостойкий бак** `harderdiesel:wear_resistant_tank`.
- Активируется предметом `harderdiesel:separator_controller`.
- Минимальная высота: `ModConfig.SEPARATOR_MIN_HEIGHT`.
- **1 жидкий вход**, **от 2 до 7 жидких выходов** — по слою на продукт.
- Тепло не обязательно.
- Рецепты типа `harderdiesel:separating`
  (файлы в `data/harderdiesel/recipe/separating/`).

### 5.4 Смешивание (Mechanical Mixer + Basin) — мод Create

- Стандартная машина Create: бассейн + механический миксер.
- Рецепты типа `create:mixing`
  (файлы в `data/createdieselgenerators/recipe/mixing/`).
- **Внимание (баг):** ключ `heatRequirement` (camelCase) игнорируется кодеком
  Create 6.0.10; правильный ключ — `heat_requirement`.
- Условные рецепты подключаются через `neoforge:conditions` (моды `createbb`,
  `powergrid`).

### 5.5 Генераторы (потребление топлива)

12 генераторов: 4 семейства топлива (`gasoline`, `diesel`, `gas`, `nitro`) ×
3 размера (`обычный`, `large`/модульный, `huge`/большой).

- Каждый генератор принимает **только** жидкости из своего тега
  `harderdiesel:generator_fuels/<семейство>` (см. раздел 2).
- Крафт: 4 ведра топлива семейства вокруг двигателя CDG.
- Мощность/расход определяются `fuel_type`-файлом по тегу `#c:<fluid>`.

### 5.6 Прочее

- **Танки-блоки:** `galvanized_reactor_tank` (крекинг-реактор),
  `wear_resistant_tank` (сепаратор) — база для сборки мультиблоков.
- **Асфальт** (`createdieselgenerators:asphalt_block`) — выход смешивания
  гудрона с гравием/песком.
- **Полимеры:** `polyethylene`, `polypropylene`, `synthetic_rubber` — предметы
  из полимеризации олефинов.

---

## 6. Выводы для переписывания дерева крафтов

1. **Живая цепочка нефтепереработки:** 6 сортов сырья (по биомам) → перегонка →
   сернистые фракции → очистка (+H2) → бензин/дизель/нитро → крекинг вакуумного
   остатка → VGO → FCC → алкилат/ароматика/полимеры; LPG → пиролиз → олефины →
   полимеры/каучук; гудрон → коксование → кокс + нефтяной газ.
2. **Сера** выделяется только при очистке сернистых фракций (из дистилляции
   кислых сортов убрана) и используется в серной кислоте и каучуке.
3. **Серная кислота** заменяет кислоту Power Grid в рецепте нитроалканов (всё ещё
   требует `powergrid` для производства самой кислоты).
4. **Mixing-нагрев сломан** для старых рецептов (`heatRequirement`); новые пишутся
   через `heat_requirement`. Коксование выполняется в смесителе, т.к. крекинг-
   реактор выводит только жидкости (предметные выходы невозможны).
5. **Остаточные сироты:** биомовый спавн 6 сортов сырья; применения нет у
   `coke_coal`, `polyethylene`, `polypropylene`, `synthetic_rubber` (производятся,
   но никуда не входят). `lpg_gas` удалён; тестовый рецепт удалён.
6. Ограничения машин: перегонка — 1 вход / до 6 выходов; крекинг — 2 входа /
   до 6 выходов (только жидкости); сепаратор — 1 вход / до 7 выходов (мин. 2);
   смешивание — стандартный бассейн Create.

---

## 7. Create: Propulsion — топливо для Thruster

Источник: `src/main/resources/data/harderdiesel/thruster_fuels/`.
Файлы подхватываются Create: Propulsion автоматически (датапак `thruster_fuels`).
Формат: `{ "fluid": "harderdiesel:<name>", "thrust_multiplier": float, "consumption_multiplier": float }`.

| Жидкость | thrust_multiplier | consumption_multiplier | Комментарий |
|---|---|---|---|
| nitromethane | 1.50 | 1.30 | Нитро, максимальная тяга |
| mixed_nitroalkanes | 1.45 | 1.28 | Нитро-смесь |
| nitroethane | 1.40 | 1.25 | Нитро |
| artisan_high_octane_gasoline | 1.40 | 0.80 | Премиум, кустарный |
| high_octane_gasoline | 1.30 | 0.85 | Премиум |
| propane | 1.25 | 1.10 | Газ, высокая тяга |
| aviation_fuel | 1.20 | 0.88 | Авиатопливо |
| kerosene | 1.15 | 0.90 | Керосин |
| ethylene | 1.10 | 1.05 | Лёгкий газ |
| low_octane_gasoline | 1.05 | 0.95 | Дешёвый бензин |
| high_cetane_diesel | 1.00 | 0.85 | Эффективный дизель |
| naphtha | 1.00 | 1.00 | Базовый (эталон) |
| medium_cetane_diesel | 0.95 | 0.90 | Средний дизель |
| low_cetane_diesel | 0.90 | 0.95 | Дешёвый дизель |

**Зависимость:** файлы работают при наличии Create: Propulsion. При отсутствии мода
JSON-файлы просто игнорируются (не вызывают ошибок).

**Статус:** Create: Propulsion не имеет сборки для MC 1.21.1. Интеграция подготовлена
и заработает при портировании Propulsion на 1.21.1.
