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
| heavy_oil | 1400 | 4000 |
| tar | 1100 | 8000 |
| low_octane_gasoline | 740 | 900 |
| high_octane_gasoline | 760 | 800 |
| artisan_high_octane_gasoline | 780 | 750 |
| nitromethane | 1120 | 700 |
| propane | 500 | 350 |
| mixed_nitroalkanes | 1050 | 650 |
| nitroethane | 1050 | 680 |
| nitropropane | 990 | 620 |
| low_cetane_diesel | 830 | 2400 |
| medium_cetane_diesel | 850 | 2600 |
| high_cetane_diesel | 870 | 2800 |
| mazut | 950 | 5000 |
| butane_gas | 560 | 250 |
| butane_liquid | 600 | 300 |
| lpg_gas | 550 | 240 |
| lpg_liquid | 620 | 320 |
| wet_gas | 580 | 280 |
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
| nitromethane | 12288 | 18432 | 36864 | 0.05 | 1.50 |
| nitroethane | 11264 | 16896 | 33792 | 0.05 | 1.00 |
| nitropropane | 10240 | 15360 | 30720 | 0.05 | 1.00 |
| mixed_nitroalkanes | 10752 | 16128 | 32256 | 0.05 | 1.00 |
| low_cetane_diesel | 5120 | 7168 | 14336 | 0.05 | 0.50 |
| medium_cetane_diesel | 6144 | 9216 | 18432 | 0.045 | 0.55 |
| high_cetane_diesel | 8192 | 12288 | 24576 | 0.04 | 0.60 |
| propane | 6144 | 9216 | 18432 | 0.05 | 1.00 |
| butane_gas | 6144 | 9216 | 18432 | 0.05 | 1.00 |
| butane_liquid | 6144 | 9216 | 18432 | 0.05 | 1.00 |
| lpg_gas | 7168 | 10240 | 20480 | 0.05 | 1.00 |
| lpg_liquid | 7168 | 10240 | 20480 | 0.05 | 1.00 |
| wet_gas | 4096 | 6144 | 12288 | 0.05 | 1.00 |
| fcc_gas | 5120 | 7680 | 15360 | 0.05 | 1.00 |
| tar | 1024 | 1280 | 2048 | 0.05 | 0.30 |

**Теги генераторов** (`data/harderdiesel/tags/fluid/generator_fuels/`):

- `gasoline`: `#c:low_octane_gasoline`, `#c:high_octane_gasoline`,
  `#c:artisan_high_octane_gasoline`, `#c:kerosene`
- `diesel`: `#c:low_cetane_diesel`, `#c:medium_cetane_diesel`, `#c:high_cetane_diesel`
- `gas`: `#c:propane`, `#c:butane_gas`, `#c:butane_liquid`, `#c:lpg_gas`,
  `#c:lpg_liquid`, `#c:wet_gas`, `#c:fcc_gas`
- `nitro`: `#c:nitromethane`, `#c:nitroethane`, `#c:nitropropane`,
  `#c:mixed_nitroalkanes`

**Тег `c:fuel`** (`data/c/tags/fluid/fuel.json`): kerosene, tar, low_octane_gasoline,
low_cetane_diesel, medium_cetane_diesel, high_octane_gasoline, high_cetane_diesel,
artisan_high_octane_gasoline, nitromethane (вместе с `flowing_*`).

---

## 3. Все крафты (20 файлов рецептов)

### 3.1 Перегонка / Ректификация — `data/createdieselgenerators/recipe/distillation/` (9)

| Файл | Вход (100 mb) | Тепло | Время | Выход |
|---|---|---|---|---|
| crude_oil | `#c:crude_oil` | heated | 60 | нафта 40 + керосин 20 + тяж.нефть 25 + пропан 10 |
| superheated_crude_oil | `#c:crude_oil` | superheated | 40 | нафта 60 + керосин 40 + тяж.нефть 40 + пропан 15 |
| heavy_oil | тяж.нефть | heated | 120 | мазут 70 + нафта 10 |
| superheated_heavy_oil | тяж.нефть | superheated | 80 | мазут 80 + нафта 15 |
| mazut | мазут | heated | 120 | гудрон 70 + нафта 5 |
| superheated_mazut | мазут | superheated | 80 | гудрон 80 + нафта 8 |
| naphtha | нафта | heated | 60 | низкоокт.бензин 60 + низкоцет.дизель 30 |
| superheated_naphtha | нафта | superheated | 40 | низкоокт.бензин 70 + низкоцет.дизель 40 |
| superheated_mixed_nitroalkanes | нитроалканы | superheated | 100 | нитрометан 30 + нитроэтан 40 + нитропропан 30 |

### 3.2 Крекинг — `data/harderdiesel/recipe/cracking/` (1)

| Файл | Вход | Тепло | Время | Выход |
|---|---|---|---|---|
| crude_oil_water_to_nitromethane | `#c:crude_oil` 100 + вода 50 | heated | 120 | нитрометан 100 |

### 3.3 Сепарация — `data/harderdiesel/recipe/separating/` (3)

| Файл | Вход | Тепло | Время | Выход |
|---|---|---|---|---|
| wet_gas | влажный газ 100 | нет | 60 | пропан 50 + бутановый газ 50 |
| mixed_nitroalkanes | нитроалканы 100 | нет | 100 | нитрометан 30 + нитроэтан 40 + нитропропан 30 |
| test_nitromethane_to_7 | нитрометан 100 | нет | 60 | нафта 15 + керосин 15 + тяж.нефть 15 + гудрон 15 + низкоокт. 15 + низкоцет. 15 + пропан 10 |

### 3.4 Смешивание — `data/createdieselgenerators/recipe/mixing/` (7)

| Файл | Вход | Тепло* | Выход |
|---|---|---|---|
| high_octane_gasoline | низкоокт.бензин 100 + этанол 50 | нет | высокоокт.бензин 100 |
| artisan_high_octane_gasoline | низкоокт.бензин 100 + нитрометан 10 | нет | кустарный высокоокт.бензин 100 |
| medium_cetane_diesel **[createbb]** | низкоцет.дизель 100 + водород 10 | heated | среднецет.дизель 100 |
| high_cetane_diesel_nitroethane | среднецет.дизель 100 + нитроэтан 20 | heated | высокоцет.дизель 100 |
| high_cetane_diesel_nitropropane | среднецет.дизель 100 + нитропропан 10 | heated | высокоцет.дизель 100 |
| mixed_nitroalkanes **[powergrid]** | пропан 25 + кислота 25 | heated | нитроалканы 35 |
| asphalt_block | гравий×2 + песок×2 + гудрон 100 | heated | асфальт×4 |

\* **Внимание (баг):** в Create 6.0.10 `create:mixing` парсится через кодек с
ключом `heat_requirement` (snake_case), а в рецептах стоит `heatRequirement`
(camelCase) — он игнорируется. Все «нагреваемые» mixing-рецепты сейчас
работают **без нагрева**.

**Условные рецепты:** `medium_cetane_diesel` — только при моде `createbb`
(Create Broken Bad, водород); `mixed_nitroalkanes` — только при моде `powergrid`
(кислота).

### 3.5 Прочие крафты (контроллеры и генераторы)

| Крафт | Итог |
|---|---|
| `PCP/AIA` (P=fluid_pipe, C=clock, A=andesite_alloy, I=iron_plate) | `cracking_controller` ×4 |
| `PCP/AIA` (C=compass) | `separator_controller` ×4 |
| «Ведро топлива» вокруг двигателя CDG (все 12 генераторов) | генератор нужного семейства/размера |

---

## 4. Сводка по каждой жидкости (производство / потребление / статус)

Статусы:
- ✅ — включена в работающее дерево;
- ⚠️ — только топливо / не производится / не потребляется;
- ❌ — сирота (нет ни крафта, ни применения).

| Жидкость | Производится из | Потребляется в | fuel_type | Теги генер. | Статус |
|---|---|---|---|---|---|
| naphtha | перегонка сырца / тяж.нефти / мазута | перегонка нафты | — | — | ✅ |
| kerosene | перегонка сырца | — | ✅ | gasoline, c:fuel | ⚠️ только топливо |
| heavy_oil | перегонка сырца | перегонка | — | — | ✅ |
| tar | перегонка мазута | асфальт | ✅ | c:fuel | ✅ |
| low_octane_gasoline | перегонка нафты | 2 mixing | ✅ | gasoline, c:fuel | ✅ |
| high_octane_gasoline | mixing | — | ✅ | gasoline, c:fuel | ✅ |
| artisan_high_octane_gasoline | mixing | — | ✅ | gasoline, c:fuel | ✅ |
| nitromethane | крекинг, сепарация | артизан-бензин, тест | ✅ | nitro, c:fuel | ✅ |
| propane | перегонка, сепарация | нитроалканы | ✅ | gas | ✅ |
| mixed_nitroalkanes | mixing | сепарация, перегонка | ✅ | nitro | ✅ |
| nitroethane | сепарация | высокоцет.дизель | ✅ | nitro | ✅ |
| nitropropane | сепарация | высокоцет.дизель | ✅ | nitro | ✅ |
| low_cetane_diesel | перегонка нафты | среднецет.дизель (createbb) | ✅ | diesel, c:fuel | ✅ |
| medium_cetane_diesel | mixing | высокоцет.дизель ×2 | ✅ | diesel, c:fuel | ✅ |
| high_cetane_diesel | mixing ×2 | — | ✅ | diesel, c:fuel | ✅ |
| mazut | перегонка тяж.нефти | перегонка | — | — | ✅ |
| butane_gas | сепарация влажн.газа | — | ✅ | gas | ⚠️ только топливо |
| butane_liquid | **нет** | — | ✅ | gas | ❌ сирота (топливо) |
| lpg_gas | **нет** | — | ✅ | gas | ❌ сирота (топливо) |
| lpg_liquid | **нет** | — | ✅ | gas | ❌ сирота (топливо) |
| wet_gas | **нет нигде** | сепарация | ✅ | gas | ⚠️ потребляется, но не производится |
| propane_liquid | **нет** | — | ❌ нет | ❌ нет | ❌ полная сирота |
| vacuum_gas_oil | **нет** | — | ❌ | ❌ | ❌ сирота |
| vacuum_residue | **нет** | — | ❌ | ❌ | ❌ сирота |
| cracked_naphtha | **нет** | — | ❌ | ❌ | ❌ сирота |
| light_cycle_oil | **нет** | — | ❌ | ❌ | ❌ сирота |
| fcc_gas | **нет** | — | ✅ | gas | ⚠️ только топливо (без производства) |
| alkylate | **нет** | — | ❌ | ❌ | ❌ сирота |
| glycerol | **нет** | — | ❌ | ❌ | ❌ сирота |
| nitroglycerin | **нет** | — | ❌ | ❌ | ❌ сирота |
| reformate | **нет** | — | ❌ | ❌ | ❌ сирота |
| benzene | **нет** | — | ❌ | ❌ | ❌ сирота |
| toluene | **нет** | — | ❌ | ❌ | ❌ сирота |
| xylene | **нет** | — | ❌ | ❌ | ❌ сирота |
| light_sweet_crude | **нет** (биомы не подключены) | — | ❌ | ❌ | ❌ сирота |
| light_sour_crude | **нет** | — | ❌ | ❌ | ❌ сирота |
| medium_sweet_crude | **нет** | — | ❌ | ❌ | ❌ сирота |
| medium_sour_crude | **нет** | — | ❌ | ❌ | ❌ сирота |
| heavy_sweet_crude | **нет** | — | ❌ | ❌ | ❌ сирота |
| heavy_sour_crude | **нет** | — | ❌ | ❌ | ❌ сирота |
| molten_sulfur | **нет** | — | ❌ | ❌ | ❌ сирота |

**Внешние жидкости, используемые как вход:**
- `c:crude_oil` — сырая нефть из CDG (тег `c:crude_oil`);
- `minecraft:water`;
- `createdieselgenerators:ethanol` — этанол (CDG);
- `createbb:hydrogen` — водород (Create Broken Bad);
- `powergrid:acid` — кислота (Power Grid).

**Предметы:** `coke_coal` — зарегистрирован, но не имеет ни крафта, ни применения.

---

## 5. Производственные блоки

### 5.1 Ректификационная колонна (Distillation Tower) — мод CDG

- Базовый блок: нефтяная бочка CDG (`createdieselgenerators:oil_barrel`);
  активируется **контроллером дистилляции CDG** (не наш).
- Мультиблок из бочек, объединённых `ConnectivityHandler`.
- **1 жидкий вход** (в нижний блок), **до 6 выходов** — по слою на каждый продукт.
- Требует нагрев снизу; рецепты бывают `heated` / `superheated`.
- Управляется рецептами типа `createdieselgenerators:distillation`
  (ключи JSON: `ingredients`, `results`, `heat_requirement`, `processing_time`).
- В нашем моде рецепты колонны лежат в
  `data/createdieselgenerators/recipe/distillation/`.

### 5.2 Крекинг-реактор (Cracking Reactor) — наш мультиблок

- Базовый блок: **оцинкованный бак реактора** `harderdiesel:galvanized_reactor_tank`
  (блок + предмет, прочность 3.0, `noOcclusion`, металлический звук).
- Активируется предметом `harderdiesel:cracking_controller` (ПКМ по структуре);
  баки заменяются блоками `cracking_reactor`.
- Крафт контроллера: `PCP/AIA` — fluid_pipe, clock, andesite_alloy, iron_plate → ×4.
- Минимальная высота: `ModConfig.CRACKING_MIN_HEIGHT` (по умолчанию 3, диапазон 2–7).
- **2 жидких входа** (вход 1 — нижний блок, вход 2 — второй снизу),
  **до 6 жидких выходов** (только жидкости, предметы не поддерживаются),
  по одному слою на продукт (слой `i` → `pos.above(i+1)`).
- Тепло подаётся снизу (Blaze Burner / горелки CDG); `heat_requirement` обязателен
  в рецептах (`none`/`heated`/`superheated`), проверяется через `BlazeBurnerBlock.HeatLevel`.
- Единый мультитанк (`CrackingReactorFluidHandler` — до нескольких разных жидкостей
  в одном слое, ёмкость по площади `width*width`).
- Рецепты типа `harderdiesel:cracking`
  (файлы в `data/harderdiesel/recipe/cracking/`, ключи: `ingredients`,
  `results`, `heat_requirement`, `processing_time`).
- JEI-категория и Ponder-сцена подключены.

### 5.3 Сепаратор (Separator) — наш мультиблок

- Базовый блок: **износостойкий бак** `harderdiesel:wear_resistant_tank`.
- Активируется предметом `harderdiesel:separator_controller`
  (крафт: `PCP/AIA` с compass → ×4).
- Минимальная высота: `ModConfig.SEPARATOR_MIN_HEIGHT` (по умолчанию 3, диапазон 2–8).
- **1 жидкий вход** (нижний блок), **от 2 до 7 жидких выходов** —
  по слою на каждый продукт.
- Тепло не обязательно (но рецепты могут его указывать).
- Рецепты типа `harderdiesel:separating`
  (файлы в `data/harderdiesel/recipe/separating/`); валидация требует ≥2 выходов.
- JEI-категория и Ponder-сцена подключены.

### 5.4 Смешивание (Mechanical Mixer + Basin) — мод Create

- Стандартная машина Create: бассейн (Basin) + механический миксер.
- Рецепты типа `create:mixing`
  (файлы в `data/createdieselgenerators/recipe/mixing/`).
- **Внимание (баг):** в рецептах ключ `heatRequirement` (camelCase) игнорируется
  кодеком Create 6.0.10; правильный ключ — `heat_requirement`.
- Условные рецепты подключаются через `neoforge:conditions` (моды `createbb`,
  `powergrid`).

### 5.5 Генераторы (потребление топлива)

12 генераторов: 4 семейства топлива (`gasoline`, `diesel`, `gas`, `nitro`) ×
3 размера (`обычный`, `large`/модульный, `huge`/большой).

- Блоки: `gasoline_generator`, `diesel_generator`, `gas_generator`,
  `nitro_generator`, `large_*`, `huge_*`.
- Каждый генератор принимает **только** жидкости из своего тега
  `harderdiesel:generator_fuels/<семейство>` (см. раздел 2); залить «не своё»
  топливо нельзя (`FuelCategory.accepts`).
- Крафт: 4 ведра топлива семейства вокруг двигателя CDG
  (`diesel_engine` / `large_diesel_engine` / `huge_diesel_engine`).
- Мощность/расход определяются `fuel_type`-файлом
  (`data/harderdiesel/createdieselgenerators/fuel_type/<fluid>.json`) по тегу
  `#c:<fluid>`.

### 5.6 Прочее

- **Танки-блоки:** `galvanized_reactor_tank` (оцинкованный, для крекинг-реактора)
  и `wear_resistant_tank` (износостойкий, для сепаратора) — блоки с
  `noOcclusion`, `isRedstoneConductor=true`, металлическим звуком; служат базой
  для сборки мультиблоков.
- **Асфальт** (`createdieselgenerators:asphalt_block`) — выход смешивания
  гудрона с гравием/песком.

---

## 6. Выводы для переписывания дерева крафтов

1. **23 из 41 жидкости «мёртвые»**: FCC-цепочка (vacuum_gas_oil →
   cracked_naphtha + light_cycle_oil + fcc_gas → alkylate), ароматика
   (reformate → benzene/toluene/xylene), взрывчатка (glycerol →
   nitroglycerin), сера (molten_sulfur из кислой нефти), LPG-семейство
   (lpg_gas/liquid, butane_liquid, propane_liquid), все 6 сортов сырой нефти.
2. **wet_gas нигде не производится** — ветка газов оборвана на входе
   (потребляется сепаратором, но не из чего не получается).
3. **Крекинг-реактор фактически пуст** — один бессмысленный рецепт
   «нефть + вода → нитрометан».
4. **Тестовый рецепт** `test_nitromethane_to_7` висит в игре.
5. **Mixing-нагрев сломан** — `heatRequirement` вместо `heat_requirement`.
6. **Керосин** — только топливо, в дерево ни во что не входит.
7. `coke_coal` не используется; биомовый спавн 6 сортов сырья — отдельная задача.
8. Ограничения машин для нового дерева: перегонка — 1 вход / до 6 выходов;
   крекинг — 2 входа / до 6 выходов (только жидкости); сепаратор — 1 вход /
   до 7 выходов (минимум 2); смешивание — стандартный бассейн Create.
