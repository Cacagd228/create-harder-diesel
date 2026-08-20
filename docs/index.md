# Create: Harder Diesel

Реалистичная нефтепереработка для Create: Diesel Generators. Мод добавляет
собственную цепочку переработки сырой нефти: от шести сортов сырья,
различающихся по биомам, до товарных бензинов, дизелей и нитротоплив — через
перегонку, очистку и крекинг.

!!! note "Платформа"

    Для **NeoForge 1.21.1**, расширяет мод **Create Diesel Generators (CDG)**.

## Возможности

- **6 сортов сырой нефти** — «сладкие» (без серы) и «кислые» (сернистые),
  лёгкие/средние/тяжёлые, каждый добывается в своём биоме.
- **Собственная ректификационная колонна** (расширение CDG) с рецептами
  посортовой перегонки.
- **Сернистые фракции** — кислая нефть даёт сернистую нафту и сернистый керосин,
  которые нужно **очищать гидроочисткой** (с водородом) с выделением серы.
- **Топливная лестница**: нафта → низко/высокооктановый бензин; дизель →
  низко/средне/высокоцетатный; нитротоплива (нитрометан/этан/пропан).
- **Крекинг-реактор** — вакуумный остаток → вакуумный газойль + гудрон.
- **Сепаратор** — разделение смесей (нефтяной газ, нитроалканы, риформат).

---

=== "Установка и зависимости"

    ## Обязательные моды

    | Мод | Роль |
    |---|---|
    | **NeoForge** 1.21.1 | Платформа |
    | **Create** | Основа механик (мультиблоки, миксеры, трубы) |
    | **Create Diesel Generators (CDG)** | Источник ректификационной колонны, нефтяных бочек, рецептов дистилляции |

    ## Рекомендуемые

    - **JEI** — просмотр рецептов и категорий.

    ## Опциональные моды (включают дополнительные рецепты)

    | Мод | Что даёт |
    |---|---|
    | **Create Broken Bad** (`createbb`) | Водород для гидроочистки сернистых фракций и среднецетатного дизеля |
    | **Power Grid** (`powergrid`) | Кислоту для серной кислоты и нитроалканов |
    | **Create: Propulsion** | Топливо для Thruster (интеграция подготовлена, см. вкладку «Интеграция Propulsion») |

    !!! tip "Условные рецепты"

        Рецепты, помеченные `createbb`, активны только при установленном моде
        Create Broken Bad (водород); помеченные `powergrid` — при Power Grid
        (кислота). Без этих модов рецепты просто скрываются.

=== "Сырая нефть и биомы"

    ## Сорта сырой нефти и биомы

    Мод добавляет **шесть сортов сырой нефти**, различающихся по биому добычи и
    профилю выхода. «Сладкая» = без серы, «кислая» = с сернистыми соединениями.

    | Сорт | Биом | Профиль |
    |---|---|---|
    | Лёгкая сладкая | Пустыня | много нафты/керосина, мало тяжёлого |
    | Лёгкая кислая | Бэдлендс | много нафты/керосина (сернистые) |
    | Средняя сладкая | Равнины/лес | сбалансированный |
    | Средняя кислая | Болото | сбалансированный, сернистый |
    | Тяжёлая сладкая | Тайга/тундра | много тяжёлого |
    | Тяжёлая кислая | Океан | максимум тяжёлого, сернистый |

    !!! warning "Статус"

        Биомовый спавн сортов сырья пока **не подключён** — сорта добываются, но
        естественного спавна по биомам нет. Пока входом служит сырая нефть CDG
        (тег `c:crude_oil`) либо сорта вручную.

    Каждый сорт перегоняется по своему рецепту в ректификационной колонне — см.
    вкладку «Обработка».

=== "Обработка"

    === "Перегонка"

        ## Ректификационная колонна (Перегонка)

        Базовый блок — нефтяная бочка CDG (`createdieselgenerators:oil_barrel`),
        активируется **контроллером дистилляции CDG** (не нашим). Мультиблок из
        бочек, объединённых `ConnectivityHandler`.

        - **1 жидкий вход** (в нижний блок), **до 6 выходов** — по слою на каждый
          продукт.
        - Требует нагрев снизу; рецепты бывают `heated` (обычный нагрев) или
          `superheated` (сверхнагрев).
        - Управляется рецептами типа `createdieselgenerators:distillation`.

        ### Сырая нефть (CDG)

        | Вход | Тепло | Время | Выход |
        |---|---|---|---|
        | Нефть-сырец (`c:crude_oil`) | Нагрев | 60 | Нафта 40 + Керосин 20 + Тяжёлый газойль 25 + Пропан 10 |
        | Нефть-сырец | Сверхнагрев | 40 | Нафта 60 + Керосин 40 + Тяжёлый газойль 40 + Пропан 15 |

        ### Лёгкая сладкая нефть

        | Тепло | Время | Выход |
        |---|---|---|
        | Нагрев | 60 | Нафта 55 + Керосин 15 + Тяжёлый газойль 10 + Нефтяной газ 15 |
        | Сверхнагрев | 40 | Нафта 80 + Керосин 22 + Тяжёлый газойль 15 + Нефтяной газ 22 |

        ### Лёгкая кислая нефть

        | Тепло | Время | Выход |
        |---|---|---|
        | Нагрев | 60 | Сернистая нафта 45 + Сернистый керосин 15 + Тяжёлый газойль 10 + Нефтяной газ 12 |
        | Сверхнагрев | 40 | Сернистая нафта 65 + Сернистый керосин 22 + Тяжёлый газойль 15 + Нефтяной газ 17 |

        ### Средняя сладкая нефть

        | Тепло | Время | Выход |
        |---|---|---|
        | Нагрев | 60 | Нафта 32 + Керосин 20 + Тяжёлый газойль 32 |
        | Сверхнагрев | 40 | Нафта 46 + Керосин 29 + Тяжёлый газойль 46 |

        ### Средняя кислая нефть

        | Тепло | Время | Выход |
        |---|---|---|
        | Нагрев | 60 | Сернистая нафта 27 + Сернистый керосин 18 + Тяжёлый газойль 30 |
        | Сверхнагрев | 40 | Сернистая нафта 39 + Сернистый керосин 26 + Тяжёлый газойль 44 |

        ### Тяжёлая сладкая нефть

        | Тепло | Время | Выход |
        |---|---|---|
        | Нагрев | 60 | Нафта 17 + Керосин 15 + Тяжёлый газойль 47 + Вакуумный остаток 10 |
        | Сверхнагрев | 40 | Нафта 25 + Керосин 22 + Тяжёлый газойль 68 + Вакуумный остаток 15 |

        ### Тяжёлая кислая нефть

        | Тепло | Время | Выход |
        |---|---|---|
        | Нагрев | 60 | Сернистая нафта 5 + Сернистый керосин 5 + Тяжёлый газойль 55 + Вакуумный остаток 5 |
        | Сверхнагрев | 40 | Сернистая нафта 7 + Сернистый керосин 7 + Тяжёлый газойль 80 + Вакуумный остаток 7 |

        ### Фракции

        | Вход | Тепло | Время | Выход |
        |---|---|---|---|
        | Тяжёлый газойль 100 mb | Нагрев | 120 | Мазут 70 + Нафта 10 |
        | Тяжёлый газойль 100 mb | Сверхнагрев | 80 | Мазут 80 + Нафта 15 |
        | Мазут 100 mb | Нагрев | 120 | Гудрон 70 + Нафта 5 |
        | Мазут 100 mb | Сверхнагрев | 80 | Гудрон 80 + Нафта 8 |
        | Нафта 100 mb | Нагрев | 60 | Низкооктановый бензин 60 + Низкоцетатный дизель 30 |
        | Нафта 100 mb | Сверхнагрев | 40 | Низкооктановый бензин 70 + Низкоцетатный дизель 40 |
        | Смесь нитроалканов 100 mb | Сверхнагрев | 100 | Нитрометан 30 + Нитроэтан 40 + Нитропропан 30 |

        !!! tip "Сверхнагрев"

            Рецепты перегонки со сверхнагревом дают больше выходного продукта за
            меньшее время.

    === "Крекинг"

        ## Крекинг-реактор

        Мультиблок «Крекинг-реактор» строится из **оцинкованных баков**
        (`harderdiesel:galvanized_reactor_tank`), которые конвертируются
        предметом `harderdiesel:cracking_controller`.

        - Минимальная высота задаётся в конфиге (`ModConfig.CRACKING_MIN_HEIGHT`).
        - **2 жидких входа** (вход 1 — нижний блок, вход 2 — второй снизу),
          **до 6 жидких выходов** (только жидкости).
        - Тепло подаётся снизу; `heat_requirement` обязателен.
        - Рецепты типа `harderdiesel:cracking`.

        | Вход | Требование к теплу | Время | Выход |
        |---|---|---|---|
        | Вакуумный остаток 100 mb | Сверхнагрев | 120 | Вакуумный газойль 70 + Гудрон 15 |
        | LPG-жидкость 100 mb | Сверхнагрев | 180 | Этилен 35 + Пропилен 35 + Бутадиен 25 |
        | Вакуумный газойль 100 mb (FCC) | Сверхнагрев | 100 | Крекированная нафта 40 + Лёгкий цикловой газойль 30 + FCC-газ 20 |
        | Крекированная нафта 100 mb (риформинг) | Сверхнагрев | 80 | Риформат 60 |

        !!! note "Предметные выходы невозможны"

            Крекинг-реактор выводит только жидкости. Поэтому коксование гудрона
            (дающее предметы) выполняется в механическом миксере (вкладка
            «Смешивание»), а не здесь.

    === "Сепарация"

        ## Сепаратор

        Мультиблок «Сепаратор» строится из **износостойких баков**
        (`harderdiesel:wear_resistant_tank`), которые конвертируются предметом
        `harderdiesel:separator_controller`.

        - Минимальная высота задаётся в конфиге (`ModConfig.SEPARATOR_MIN_HEIGHT`).
        - **1 жидкий вход** в нижнем блоке, разделяет на **от 2 до 7 выходов** —
          по слою на продукт.
        - Тепло не требуется.
        - Рецепты типа `harderdiesel:separating`.

        | Вход | Требование к теплу | Время | Выход |
        |---|---|---|---|
        | Нефтяной газ 100 mb | Без нагрева | 60 | Пропан 50 + Бутан 50 |
        | Смесь нитроалканов 100 mb | Без нагрева | 100 | Нитрометан 30 + Нитроэтан 40 + Нитропропан 30 |
        | Риформат 100 mb (BTX) | Без нагрева | 80 | Бензол 30 + Толуол 40 + Ксилол 30 |

    === "Смешивание"

        ## Механический миксер (Смешивание)

        Стандартная машина Create: бассейн (Basin) + механический миксер. Рецепты
        типа `create:mixing`. Здесь выполняются гидроочистка, полимеризация,
        коксование и производство высокооктановых/высокоцетатных топлив.

        ### Гидроочистка (требует мод `createbb` — водород)

        | Вход | Тепло | Выход |
        |---|---|---|
        | Сернистая нафта 100 mb + Водород 10 mb | Нагрев | Нафта 95 + Сера 5 |
        | Сернистый керосин 100 mb + Водород 10 mb | Нагрев | Керосин 95 + Сера 5 |

        ### Топливная лестница

        | Вход | Условие | Тепло | Выход |
        |---|---|---|---|
        | Низкооктановый бензин 100 mb + Этанол 50 mb | — | Без нагрева | Высокооктановый бензин 100 mb |
        | Низкооктановый бензин 100 mb + Нитрометан 10 mb | — | Без нагрева | Кустарный высокооктановый бензин 100 mb |
        | Керосин 100 mb + Нитроэтан 25 mb | — | Без нагрева | Авиационное топливо 100 mb |
        | Низкоцетатный дизель 100 mb + Водород 10 mb | `createbb` | Нагрев | Среднецетатный дизель 100 mb |
        | Среднецетатный дизель 100 mb + Нитроэтан 20 mb | — | Нагрев | Высокоцетатный дизель 100 mb |
        | Среднецетатный дизель 100 mb + Нитропропан 10 mb | — | Нагрев | Высокоцетатный дизель 100 mb |
        | Среднецетатный дизель 100 mb + Лёгкий цикловой газойль 20 mb | — | Нагрев | Высокоцетатный дизель 100 mb |

        ### Кислоты и нитроалканы (требуют мод `powergrid` — кислота)

        | Вход | Тепло | Выход |
        |---|---|---|
        | Расплавленная сера 25 mb + Кислота 25 mb | Нагрев | Серная кислота 35 mb |
        | Пропан 25 mb + Серная кислота 25 mb | Нагрев | Смесь нитроалканов 35 mb |

        ### Сжижение газов и LPG

        | Вход | Тепло | Выход |
        |---|---|---|
        | Пропан 100 mb + Лёд ×1 | Без нагрева | Сжиженный пропан 10 mb |
        | Бутан 100 mb + Лёд ×1 | Без нагрева | Сжиженный бутан 10 mb |
        | Пропан 60 mb + Бутан 40 mb + Лёд ×1 | Без нагрева | LPG-жидкость 10 mb |

        ### Полимеризация, каучук, коксование

        | Вход | Тепло | Выход |
        |---|---|---|
        | FCC-газ 100 mb + Серная кислота 50 mb | Нагрев | Алкилат 100 mb |
        | Пропилен 100 mb + Вода 50 mb | Нагрев | Глицерин 60 mb |
        | Глицерин 100 mb + Серная кислота 50 mb + Кислота 50 mb (`powergrid`) | Нагрев | Нитроглицерин 100 mb |
        | Этилен 250 mb + Андезитовый сплав ×1 | Нагрев | Полиэтилен ×4 |
        | Пропилен 250 mb + Андезитовый сплав ×2 | Нагрев | Полипропилен ×4 |
        | Бутадиен 250 mb + Расплавленная сера 50 mb | Нагрев | Синтетический каучук ×4 |
        | Гудрон 100 mb (коксование) | Нагрев | Кокс ×2 + Нефтяной газ 20 mb |
        | Гравий ×2 + Песок ×2 + Гудрон 100 mb | Нагрев | Асфальтовый блок ×4 |

        !!! warning "Внимание (баг) — нагрев в миксере"

            В Create 6.0.10 `create:mixing` парсится кодеком с ключом
            `heat_requirement` (snake_case). Часть рецептов всё ещё использует
            `heatRequirement` (camelCase) — он игнорируется. Новые рецепты пишутся
            через `heat_requirement`, чтобы нагрев реально требовался.

=== "Жидкости"

    ## Жидкости

    ### Базовые статы (density / viscosity)

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

    ### Топливные статы (fuel_type)

    `speed` у всех жидкостей 96/96/224, кроме `tar` — 64/64/160. `strength`
    указан для трёх размеров генераторов (обычный / модульный / большой).

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

    ### Теги генераторов (`generator_fuels`)

    - **`gasoline`**: `low_octane_gasoline`, `high_octane_gasoline`,
      `artisan_high_octane_gasoline`, `kerosene`, `aviation_fuel`
    - **`diesel`**: `low_cetane_diesel`, `medium_cetane_diesel`, `high_cetane_diesel`
    - **`gas`**: `propane`, `propane_liquid`, `butane`, `butane_liquid`,
      `lpg_liquid`, `petroleum_gas`, `fcc_gas`
    - **`nitro`**: `nitromethane`, `nitroethane`, `nitropropane`,
      `mixed_nitroalkanes`

    ### Сводка по каждой жидкости

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

    ### Внешние жидкости (входы)

    - `c:crude_oil` — сырая нефть из CDG;
    - `minecraft:water`;
    - `createdieselgenerators:ethanol` — этанол (CDG);
    - `createbb:hydrogen` — водород (Create Broken Bad);
    - `powergrid:acid` — кислота (Power Grid, для серной кислоты).

=== "Топлива и генераторы"

    ## Топлива и генераторы

    ### Топливная лестница

    От дешёвых базовых топлив к премиальным нитротопливам:

    ```
    Нафта ──перегонка──▶ Низкооктановый бензин (LOG) + Низкоцетатный дизель (LCD)
    LOG + этанол ──▶ Высокооктановый бензин (HOG)
    LOG + нитрометан ──▶ Кустарный высокооктановый бензин (AHOG)
    LCD + водород ──▶ Среднецетатный дизель (MCD)
    MCD + нитроэтан / нитропропан / LCO ──▶ Высокоцетатный дизель (HCD)
    Керосин + нитроэтан ──▶ Авиационное топливо
    ```

    ### Генераторы

    12 генераторов: **4 семейства топлива** (`gasoline`, `diesel`, `gas`,
    `nitro`) × **3 размера** (обычный, `large`/модульный, `huge`/большой).

    - Каждый генератор принимает **только** жидкости из своего тега
      `harderdiesel:generator_fuels/<семейство>` (см. вкладку «Жидкости»).
    - Крафт: **4 ведра топлива** семейства вокруг двигателя CDG.
    - Мощность/расход определяются `fuel_type`-файлом по тегу `#c:<fluid>`.

    | Семейство | Обычный | Large | Huge |
    |---|---|---|---|
    | gasoline | `gasoline_generator` | `large_gasoline_generator` | `huge_gasoline_generator` |
    | diesel | `diesel_generator` | `large_diesel_generator` | `huge_diesel_generator` |
    | gas | `gas_generator` | `large_gas_generator` | `huge_gas_generator` |
    | nitro | `nitro_generator` | `large_nitro_generator` | `huge_nitro_generator` |

    ### Сравнение семейств

    | Семейство | Топлива | Комментарий |
    |---|---|---|
    | **gasoline** | LOG, HOG, AHOG, kerosene, aviation_fuel | Бензины + керосин и авиатопливо |
    | **diesel** | LCD, MCD, HCD | Дизельная лестница |
    | **gas** | пропан/бутан (газ и сжиженный), LPG, нефт.газ, FCC-газ | Газы |
    | **nitro** | нитрометан, нитроэтан, нитропропан, нитроалканы | Нитротоплива |

    !!! tip "Авиационное топливо"

        Авиатопливо — бензиновое семейство: горит дольше (`burn_rate 0.02`), даёт
        меньше СУ, но выигрывает по времени/СУ.

=== "Интеграция Propulsion"

    ## Интеграция Create: Propulsion

    Файлы в `data/harderdiesel/thruster_fuels/` задают характеристики топлива для
    Thruster мода Create: Propulsion (датапак `thruster_fuels`).

    Формат:
    ```json
    { "fluid": "harderdiesel:<name>", "thrust_multiplier": float, "consumption_multiplier": float }
    ```

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

    ### Зависимость и статус

    - Файлы работают при наличии **Create: Propulsion**. При отсутствии мода
      JSON-файлы просто игнорируются (не вызывают ошибок).
    - **Статус:** Create: Propulsion пока не имеет сборки для MC 1.21.1.
      Интеграция подготовлена и заработает при портировании Propulsion на 1.21.1.

=== "Таблицы крафтов"

    ## Таблицы крафтов

    Сводные таблицы всех рецептов мода. Объёмы жидкостей — в миллилитрах (mb).
    «Нагрев» — требование к теплу (обычный нагрев / сверхнагрев).

    ### Крекинг (Cracking Reactor)

    | Вход | Тепло | Время | Выход |
    |---|---|---|---|
    | Вакуумный остаток 100 mb | Сверхнагрев | 120 | Вакуумный газойль 70 + Гудрон 15 |
    | LPG-жидкость 100 mb | Сверхнагрев | 180 | Этилен 35 + Пропилен 35 + Бутадиен 25 |
    | Вакуумный газойль 100 mb (FCC) | Сверхнагрев | 100 | Крекированная нафта 40 + Лёгкий цикловой газойль 30 + FCC-газ 20 |
    | Крекированная нафта 100 mb (риформинг) | Сверхнагрев | 80 | Риформат 60 |

    ### Сепарация (Separator)

    | Вход | Тепло | Время | Выход |
    |---|---|---|---|
    | Нефтяной газ 100 mb | Без нагрева | 60 | Пропан 50 + Бутан 50 |
    | Смесь нитроалканов 100 mb | Без нагрева | 100 | Нитрометан 30 + Нитроэтан 40 + Нитропропан 30 |
    | Риформат 100 mb (BTX) | Без нагрева | 80 | Бензол 30 + Толуол 40 + Ксилол 30 |

    ### Перегонка (Distillation Tower)

    #### Сырьё

    | Вход | Тепло | Время | Выход |
    |---|---|---|---|
    | Нефть-сырец (`c:crude_oil`) | Нагрев | 60 | Нафта 40 + Керосин 20 + Тяжёлый газойль 25 + Пропан 10 |
    | Нефть-сырец | Сверхнагрев | 40 | Нафта 60 + Керосин 40 + Тяжёлый газойль 40 + Пропан 15 |

    #### Биомовые сорта (по 2 строки на сорт: нагрев / сверхнагрев)

    | Сорт | Тепло | Время | Выход |
    |---|---|---|---|
    | Лёгкая сладкая | Нагрев | 60 | Нафта 55 + Керосин 15 + ТГ 10 + Нефт.газ 15 |
    | Лёгкая сладкая | Сверхнагрев | 40 | Нафта 80 + Керосин 22 + ТГ 15 + Нефт.газ 22 |
    | Лёгкая кислая | Нагрев | 60 | Серн.нафта 45 + Серн.керосин 15 + ТГ 10 + Нефт.газ 12 |
    | Лёгкая кислая | Сверхнагрев | 40 | Серн.нафта 65 + Серн.керосин 22 + ТГ 15 + Нефт.газ 17 |
    | Средняя сладкая | Нагрев | 60 | Нафта 32 + Керосин 20 + ТГ 32 |
    | Средняя сладкая | Сверхнагрев | 40 | Нафта 46 + Керосин 29 + ТГ 46 |
    | Средняя кислая | Нагрев | 60 | Серн.нафта 27 + Серн.керосин 18 + ТГ 30 |
    | Средняя кислая | Сверхнагрев | 40 | Серн.нафта 39 + Серн.керосин 26 + ТГ 44 |
    | Тяжёлая сладкая | Нагрев | 60 | Нафта 17 + Керосин 15 + ТГ 47 + Вак.остаток 10 |
    | Тяжёлая сладкая | Сверхнагрев | 40 | Нафта 25 + Керосин 22 + ТГ 68 + Вак.остаток 15 |
    | Тяжёлая кислая | Нагрев | 60 | Серн.нафта 5 + Серн.керосин 5 + ТГ 55 + Вак.остаток 5 |
    | Тяжёлая кислая | Сверхнагрев | 40 | Серн.нафта 7 + Серн.керосин 7 + ТГ 80 + Вак.остаток 7 |

    (ТГ = Тяжёлый газойль.)

    #### Фракции

    | Вход | Тепло | Время | Выход |
    |---|---|---|---|
    | Тяжёлый газойль 100 mb | Нагрев | 120 | Мазут 70 + Нафта 10 |
    | Тяжёлый газойль 100 mb | Сверхнагрев | 80 | Мазут 80 + Нафта 15 |
    | Мазут 100 mb | Нагрев | 120 | Гудрон 70 + Нафта 5 |
    | Мазут 100 mb | Сверхнагрев | 80 | Гудрон 80 + Нафта 8 |
    | Нафта 100 mb | Нагрев | 60 | Низкооктановый бензин 60 + Низкоцетатный дизель 30 |
    | Нафта 100 mb | Сверхнагрев | 40 | Низкооктановый бензин 70 + Низкоцетатный дизель 40 |
    | Смесь нитроалканов 100 mb | Сверхнагрев | 100 | Нитрометан 30 + Нитроэтан 40 + Нитропропан 30 |

    ### Смешивание (Mechanical Mixer)

    | Вход | Условие | Тепло | Выход |
    |---|---|---|---|
    | Гравий ×2 + Песок ×2 + Гудрон 100 mb | — | Нагрев | Асфальтовый блок ×4 |
    | Низкооктановый бензин 100 mb + Этанол 50 mb | — | Без нагрева | Высокооктановый бензин 100 mb |
    | Низкооктановый бензин 100 mb + Нитрометан 10 mb | — | Без нагрева | Кустарный высокооктановый бензин 100 mb |
    | Керосин 100 mb + Нитроэтан 25 mb | — | Без нагрева | Авиационное топливо 100 mb |
    | Низкоцетатный дизель 100 mb + Водород 10 mb | `createbb` | Нагрев | Среднецетатный дизель 100 mb |
    | Среднецетатный дизель 100 mb + Нитроэтан 20 mb | — | Нагрев | Высокоцетатный дизель 100 mb |
    | Среднецетатный дизель 100 mb + Нитропропан 10 mb | — | Нагрев | Высокоцетатный дизель 100 mb |
    | Среднецетатный дизель 100 mb + Лёгкий цикловой газойль 20 mb | — | Нагрев | Высокоцетатный дизель 100 mb |
    | Пропан 25 mb + Серная кислота 25 mb | `powergrid` | Нагрев | Смесь нитроалканов 35 mb |
    | Расплавленная сера 25 mb + Кислота 25 mb | `powergrid` | Нагрев | Серная кислота 35 mb |
    | Сернистая нафта 100 mb + Водород 10 mb | `createbb` | Нагрев | Нафта 95 + Сера 5 |
    | Сернистый керосин 100 mb + Водород 10 mb | `createbb` | Нагрев | Керосин 95 + Сера 5 |
    | Пропан 100 mb + Лёд ×1 | — | Без нагрева | Сжиженный пропан 10 mb |
    | Бутан 100 mb + Лёд ×1 | — | Без нагрева | Сжиженный бутан 10 mb |
    | Пропан 60 mb + Бутан 40 mb + Лёд ×1 | — | Без нагрева | LPG-жидкость 10 mb |
    | FCC-газ 100 mb + Серная кислота 50 mb | — | Нагрев | Алкилат 100 mb |
    | Пропилен 100 mb + Вода 50 mb | — | Нагрев | Глицерин 60 mb |
    | Глицерин 100 mb + Серная кислота 50 mb + Кислота 50 mb | `powergrid` | Нагрев | Нитроглицерин 100 mb |
    | Этилен 250 mb + Андезитовый сплав ×1 | — | Нагрев | Полиэтилен ×4 |
    | Пропилен 250 mb + Андезитовый сплав ×2 | — | Нагрев | Полипропилен ×4 |
    | Бутадиен 250 mb + Расплавленная сера 50 mb | — | Нагрев | Синтетический каучук ×4 |
    | Гудрон 100 mb (коксование) | — | Нагрев | Кокс ×2 + Нефтяной газ 20 mb |

    ### Примечания

    - Рецепты `createbb` активны при моде Create Broken Bad (водород); `powergrid`
      — при Power Grid (кислота).
    - Серная кислота производится из расплавленной серы и кислоты Power Grid и
      заменяет обычную кислоту в рецепте нитроалканов.
    - Коксование гудрона выполнено в смесителе (крекинг-реактор выводит только
      жидкости, предметные выходы невозможны).
    - Рецепты перегонки со сверхнагревом дают больше выходного продукта за
      меньшее время.

=== "Разработчикам"

    ## Пособие по мультиблокам

    Практическое руководство по созданию Create-мультиблоков (NeoForge 1.21.1),
    написанное по мотивам реальной реализации **Крекинг-реактора** в моде
    `harderdiesel` — порта «Ректификационной колонны» (Distillation Tower) из
    мода **Create: Diesel Generators** (CDG, `createdieselgenerators`).

    === "Введение и архитектура"

        ## 1. Введение

        Мультиблок в Create — это группа блоков, которые объединяются в одну
        машину через систему `ConnectivityHandler`. Типичный пример — жидкостный
        бак (`FluidTank`), ректификационная колонна CDG, ферментер. Один блок
        становится **контроллером**, остальные — **частями**; логика
        обрабатывается только в контроллере, а части делегируют к нему.

        Ключевые классы и интерфейсы Create:

        | Класс/интерфейс | Назначение |
        |---|---|
        | `IBE<BE>` | Связывает `Block` с `BlockEntity` (`getBlockEntityClass`, `getBlockEntityType`, `withBlockEntityDo`) |
        | `SmartBlockEntity` | База BE с `tick()`, `read/write`, `sendData`, поведенциями |
        | `IMultiBlockEntityContainer.Fluid` | Контракт мультиблока: контроллер, размеры, танки |
        | `ConnectivityHandler` | `formMulti`, `splitMulti`, `partAt`, `isConnected` |
        | `CreateRegistrate` / `DeferredRegister` | Регистрация (в этом проекте — `DeferredRegister`) |

        Сквозной пример этого пособия — **Крекинг-реактор**:

        | Наш файл | Аналог в CDG (образец) |
        |---|---|
        | `content/cracking/CrackingReactorBlock.java` | `content/distillation/DistillationTankBlock.java` |
        | `content/cracking/CrackingReactorBlockEntity.java` | `content/distillation/DistillationTankBlockEntity.java` |
        | `content/cracking/CrackingControllerItem.java` | `content/distillation/DistillationControllerItem.java` |
        | `content/cracking/CrackingRecipe.java` | `content/distillation/DistillationRecipe.java` |
        | `ModRecipeTypes.java` | `CDGRecipes.java` |
        | `ModBlockEntityTypes.java` | `CDGBlockEntityTypes.java` |

        ## 2. Общая архитектура

        Мультиблок состоит из 4 слоёв:

        ```
        Блок (Block)  →  Блок-сущность (BlockEntity)  →  Контроллер-предмет  →  Рецепт
        ```

        **Поток работы:**

        1. Игрок ставит **базовые блоки** (в нашем случае — нефтяные бочки CDG).
        2. ПКМ **контроллером-предметом** по структуре → базовые блоки заменяются
           на блоки мультиблока (`setBlock`), вызывается `updateConnectivity()`
           (формирование через `formMulti`) и `updateVerticalMulti()`.
        3. Обработка рецептов идёт **только в контроллере** (нижний блок), части
           обслуживают жидкость/рендер/выкачивание.

        **Схема файлов мультиблока:**

        ```
        content/<machine>/
        ├── <Machine>Block.java          // блок: IBE, IWrenchable, состояния TOP/BOTTOM/SHAPE
        ├── <Machine>BlockEntity.java    // ядро: контроллер, размеры, танки, tick, рецепты
        ├── <Machine>ControllerItem.java // предмет-конвертер структуры
        └── <Machine>Recipe.java         // тип рецепта

        client/
        ├── <Machine>Renderer.java       // рендер жидкости/индикаторов
        ├── <Machine>Model.java          // CTModel (отсечение граней)
        ├── <Machine>CTBehavior.java     // connected textures
        ├── <Machine>Category.java       // JEI-категория
        └── <Machine>Scene.java          // Ponder-сцена
        ```

    === "Регистрация"

        ## 3. Регистрация

        ### 3.1 Рецепт-тип (`ModRecipeTypes.java`)

        Регистрируем enum, реализующий `IRecipeTypeInfo`, с сериализатором
        `StandardProcessingRecipe.Serializer`.

        ```java
        public enum ModRecipeTypes implements IRecipeTypeInfo {
            CRACKING(CrackingRecipe::new);

            private final ResourceLocation id;
            private final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> serializerObject;
            @Nullable
            private final DeferredHolder<RecipeType<?>, RecipeType<?>> typeObject;
            private final Supplier<RecipeType<?>> type;

            ModRecipeTypes(StandardProcessingRecipe.Factory<?> processingFactory) {
                this(() -> new StandardProcessingRecipe.Serializer<>(processingFactory));
            }

            ModRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
                String name = name().toLowerCase(Locale.ROOT);
                id = ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, name);
                serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
                typeObject = Registers.TYPE_REGISTER.register(name, () -> RecipeType.simple(id));
                type = typeObject;
            }

            public static void register(IEventBus modEventBus) {
                Registers.SERIALIZER_REGISTER.register(modEventBus);
                Registers.TYPE_REGISTER.register(modEventBus);
            }

            private static class Registers {
                private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER =
                        DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, HarderDiesel.MODID);
                private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER =
                        DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, HarderDiesel.MODID);
            }
        }
        ```

        ### 3.2 Тип блок-сущности (`ModBlockEntityTypes.java`)

        **Важно**: внутри лямбды `register(...)` нельзя ссылаться на сам
        `DeferredHolder` напрямую (self-reference). Решение — отдельные
        статические методы, вызываемые после регистрации.

        ```java
        public class ModBlockEntityTypes {
            public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
                    DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, HarderDiesel.MODID);

            public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrackingReactorBlockEntity>> CRACKING_REACTOR =
                    BLOCK_ENTITY_TYPES.register("cracking_reactor", () -> createReactorType());

            private static BlockEntityType<CrackingReactorBlockEntity> createReactorType() {
                return BlockEntityType.Builder.of(ModBlockEntityTypes::createReactor, ModBlocks.CRACKING_REACTOR.get())
                        .build(null);
            }

            private static CrackingReactorBlockEntity createReactor(BlockPos pos, BlockState state) {
                return new CrackingReactorBlockEntity(CRACKING_REACTOR.get(), pos, state);
            }
        }
        ```

        ### 3.3 Блок (`ModBlocks.java`)

        ```java
        public static final DeferredBlock<CrackingReactorBlock> CRACKING_REACTOR =
                BLOCKS.register("cracking_reactor",
                        () -> new CrackingReactorBlock(BlockBehaviour.Properties.of()
                                .mapColor(MapColor.COLOR_ORANGE)
                                .strength(3.0F)
                                .noOcclusion()
                                .isRedstoneConductor((p1, p2, p3) -> true)));
        ```

        ### 3.4 Капабилити (в `HarderDiesel.java`)

        ```java
        modEventBus.addListener((RegisterCapabilitiesEvent event) ->
                CrackingReactorBlockEntity.registerCapabilities(event));
        ```

        > Порядок регистрации в конструкторе мода важен: предметы/блоки/жидкости
        > → рецепты → BE-типы → конфиг.

    === "Блок и блок-сущность"

        ## 4. Блок

        Полный пример — `CrackingReactorBlock.java`. Основное:

        ```java
        public class CrackingReactorBlock extends Block
                implements IBE<CrackingReactorBlockEntity>, IWrenchable, SpecialBlockItemRequirement {

            public static final BooleanProperty TOP = BooleanProperty.create("top");
            public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
            public static final EnumProperty<FluidTankBlock.Shape> SHAPE =
                    EnumProperty.create("shape", FluidTankBlock.Shape.class);

            public static boolean isReactor(BlockState state) {
                return state.getBlock() instanceof CrackingReactorBlock;
            }
        }
        ```

        **Жизненный цикл блока** — критические методы:

        ```java
        @Override
        public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved) {
            if (oldState.getBlock() == state.getBlock() || moved)
                return;
            withBlockEntityDo(level, pos, CrackingReactorBlockEntity::updateConnectivity);
            withBlockEntityDo(level, pos, CrackingReactorBlockEntity::updateVerticalMulti);
        }

        @Override
        public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
            if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
                BlockEntity be = world.getBlockEntity(pos);
                if (!(be instanceof CrackingReactorBlockEntity tankBE))
                    return;
                world.removeBlockEntity(pos);
                ConnectivityHandler.splitMulti(tankBE);
            }
        }

        @Override
        public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos otherPos, boolean p_60514_) {
            super.neighborChanged(state, level, pos, block, otherPos, p_60514_);
            withBlockEntityDo(level, pos, CrackingReactorBlockEntity::updateVerticalMulti);
        }

        @Override
        public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
            if (direction == Direction.DOWN && neighbourState.getBlock() != this)
                withBlockEntityDo(level, pos, CrackingReactorBlockEntity::updateTemperature);
            return super.updateShape(state, direction, neighbourState, level, pos, neighbourPos);
        }
        ```

        Пояснение:
        - `onPlace` → `updateConnectivity()` (формирует мультиблок) +
          `updateVerticalMulti()` (TOP/BOTTOM).
        - `onRemove` → `splitMulti()` (разбивает мультиблок, сохраняя жидкость).
        - `neighborChanged` → обновление вертикальной связности.
        - `updateShape(DOWN)` → пересчёт температуры (для тепла снизу).

        Реализация интерфейса `IBE`:

        ```java
        @Override
        public Class<CrackingReactorBlockEntity> getBlockEntityClass() {
            return CrackingReactorBlockEntity.class;
        }

        @Override
        public BlockEntityType<? extends CrackingReactorBlockEntity> getBlockEntityType() {
            return ModBlockEntityTypes.CRACKING_REACTOR.get();
        }
        ```

        ## 5. Блок-сущность: ядро мультиблока

        Самый важный файл — `CrackingReactorBlockEntity.java`. Класс:

        ```java
        public class CrackingReactorBlockEntity extends SmartBlockEntity
                implements IMultiBlockEntityContainer.Fluid, IHaveGoggleInformation, IHaveHoveringInformation {
            private static final int MAX_SIZE = 3;

            public CrackingReactorFluidHandler tankInventory;
            protected BlockPos controller;   // позиция контроллера (null = сам контроллер)
            protected BlockPos lastKnownPos;
            protected boolean updateConnectivity;
            protected boolean updateCapability;
            public boolean window;
            public int width;
            protected int height;
            boolean tanksFull = false;
            public int processingTime = -1;
            public CrackingRecipe currentRecipe;
        }
        ```

        ### 5.1 Контроллер и части

        ```java
        @Override
        public boolean isController() {
            return controller == null ||
                    worldPosition.getX() == controller.getX() &&
                    worldPosition.getY() == controller.getY() &&
                    worldPosition.getZ() == controller.getZ();
        }

        @Override
        public void setController(BlockPos controller) {
            if (level.isClientSide && !isVirtual())
                return;
            if (controller.equals(this.controller))
                return;
            this.controller = controller;
            refreshCapability();
            setChanged();
            sendData();
        }

        @Override
        public CrackingReactorBlockEntity getControllerBE() {
            if (isController())
                return this;
            BlockEntity blockEntity = level.getBlockEntity(controller);
            return blockEntity instanceof CrackingReactorBlockEntity be ? be : null;
        }
        ```

        ### 5.2 Размеры мультиблока

        ```java
        @Override
        public Direction.Axis getMainConnectionAxis() {
            return Direction.Axis.Y;
        }

        @Override
        public int getMaxLength(Direction.Axis longAxis, int width) {
            if (longAxis == Direction.Axis.Y)
                return 1;      // formMulti объединяет ТОЛЬКО один слой по вертикали
            return getMaxWidth();
        }

        @Override
        public int getMaxWidth() {
            return MAX_SIZE;   // до 3x3
        }
        ```

        **Критично**: при `getMaxLength(Y,width)=1` `ConnectivityHandler.formMulti`
        объединяет только один **горизонтальный** слой (например, 2×2 или 3×3).
        Вертикальность (слои выше) реализуется отдельно через
        `updateVerticalMulti` и `isSameMultiBlock`.

        ### 5.3 Формирование и вертикальная связность

        ```java
        public void updateConnectivity() {
            updateConnectivity = false;
            if (level.isClientSide || !isController())
                return;
            ConnectivityHandler.formMulti(this);
        }

        public void updateVerticalMulti() {
            BlockState state = this.getBlockState();
            if (CrackingReactorBlock.isReactor(state)) {
                state = state.setValue(CrackingReactorBlock.BOTTOM, getBottomConnectivity());
                state = state.setValue(CrackingReactorBlock.TOP, getTopConnectivity());
                if (state != this.getBlockState())
                    level.setBlock(getBlockPos(), state, 3);
            }
            if (level.getBlockEntity(getBlockPos().below()) instanceof CrackingReactorBlockEntity be)
                be.updateVerticalMulti();
        }

        public boolean isBottom() {
            return !(level.getBlockEntity(getBlockPos().below()) instanceof CrackingReactorBlockEntity be
                    && isSameMultiBlock(be));
        }
        ```

        ### 5.4 Ёмкость по площади (не по высоте)

        ```java
        public void applyFluidTankSize(int blocks) {
            // Ёмкость зависит от площади (width x width), а не от высоты.
            tankInventory.setCapacity(getTotalTankSize() * getCapacityMultiplier());
            forceFluidLevelUpdate = true;
        }

        public int getTotalTankSize() {
            return width * width;
        }

        public static int getCapacityMultiplier() {
            return AllConfigs.server().fluids.fluidTankCapacity.get() * 1000;
        }
        ```

    === "Капабилити жидкости"

        ## 6. Капабилити жидкости

        ### 6.1 Распределение по слоям (`handlerForCapability`)

        Create-труба/помпа работает через `Capabilities.FluidHandler.BLOCK`
        **каждого блока**. Чтобы входы попадали в контроллер из любого нижнего
        блока, а выходы выкачивались из любого блока слоя, капабилити
        делегирует:

        ```java
        private IFluidHandler handlerForCapability() {
            if (level == null)
                return tankInventory;
            CrackingReactorBlockEntity controllerBE = getControllerBE();
            if (isBottom())
                return controllerBE != null ? controllerBE.tankInventory : tankInventory;
            if (controllerBE == null)
                return tankInventory;
            // Все блоки слоя выхода делят танк первого блока этого слоя.
            int layer = worldPosition.getY() - controllerBE.getBlockPos().getY();
            BlockPos repPos = controllerBE.getBlockPos().offset(0, layer, 0);
            BlockEntity rep = level.getBlockEntity(repPos);
            return rep instanceof CrackingReactorBlockEntity repBE ? repBE.tankInventory : tankInventory;
        }

        private void refreshCapability() {
            fluidCapability = handlerForCapability();
            invalidateCapabilities();
        }
        ```

        ### 6.2 Мультитанк (`CrackingReactorFluidHandler`)

        Один блок может хранить несколько разных жидкостей. Реализация на базе
        `SmartFluidTank` с внутренним списком `FluidTank`:

        ```java
        public static class CrackingReactorFluidHandler extends SmartFluidTank {
            int tankCount;
            NonNullList<FluidTank> tanks = NonNullList.create();

            public CrackingReactorFluidHandler(int tankCount, int capacity, Consumer<FluidStack> updateCallback) {
                super(capacity, updateCallback);
                for (int i = 0; i < tankCount; i++)
                    tanks.add(new FluidTank(capacity));
                this.tankCount = tankCount;
            }

            @Override
            public int getTanks() { return tankCount; }

            @Override
            public FluidStack getFluidInTank(int tank) { return tanks.get(tank).getFluid(); }

            @Override
            public int getTankCapacity(int tank) { return tanks.get(tank).getCapacity(); }

            @Override
            public boolean isFluidValid(int tank, FluidStack stack) { return true; }

            @Override
            public int fill(FluidStack resource, FluidAction action) {
                for (FluidTank tank : tanks) {
                    if (FluidStack.isSameFluidSameComponents(tank.getFluid(), resource)) {
                        int result = tank.fill(resource, action);
                        if (action.execute()) onContentsChanged();
                        return result;
                    }
                }
                for (FluidTank tank : tanks) {
                    if (tank.getFluid().isEmpty()) {
                        int result = tank.fill(resource, action);
                        if (action.execute()) onContentsChanged();
                        return result;
                    }
                }
                return 0;
            }

            @Override
            public FluidStack drain(FluidStack resource, FluidAction action) {
                for (FluidTank tank : tanks) {
                    if (FluidStack.isSameFluidSameComponents(tank.getFluid(), resource)) {
                        FluidStack result = tank.drain(resource, action);
                        if (action.execute()) onContentsChanged();
                        return result;
                    }
                }
                return FluidStack.EMPTY;
            }
        }
        ```

        ### 6.3 Регистрация капабилити

        ```java
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.registerBlockEntity(
                    Capabilities.FluidHandler.BLOCK,
                    ModBlockEntityTypes.CRACKING_REACTOR.get(),
                    (be, context) -> {
                        if (be.fluidCapability == null)
                            be.refreshCapability();
                        return be.fluidCapability;
                    });
        }
        ```

    === "Обработка рецептов"

        ## 7. Обработка рецептов

        ### 7.1 Поиск рецептов

        ```java
        protected List<Recipe<?>> getMatchingRecipes() {
            List<RecipeHolder<? extends Recipe<?>>> list =
                    RecipeFinder.get(getRecipeCacheKey(), level,
                            recipe -> recipe.value().getType() == ModRecipeTypes.CRACKING.getType());
            return list.stream()
                    .map(RecipeHolder::value)
                    .sorted((r1, r2) -> {
                        if (r1 instanceof CrackingRecipe recipe1 && r2 instanceof CrackingRecipe recipe2)
                            return recipe2.getRequiredHeat().ordinal() - recipe1.getRequiredHeat().ordinal();
                        return 0;
                    })
                    .filter(r -> {
                        if (r instanceof CrackingRecipe recipe) {
                            if (!recipe.getRequiredHeat().testBlazeBurner(highestHeatLevel))
                                return false;
                            return recipe.apply(this, true);
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }
        ```

        ### 7.2 Тик (обработка только в контроллере нижнего слоя)

        ```java
        @Override
        public void tick() {
            boolean prevTanksFull = tanksFull;
            tanksFull = false;
            if (isController() && isBottom()) {
                if (processingTime >= 0 && currentRecipe == null) {
                    List<Recipe<?>> r = getMatchingRecipes();
                    if (!r.isEmpty())
                        currentRecipe = (CrackingRecipe) r.get(0);
                }

                if (processingTime > -1 && currentRecipe != null) {
                    if (currentRecipe.apply(this, true)) {
                        processingTime--;
                    } else {
                        tanksFull = true;
                    }
                    if (!(hasIngredients()
                            && currentRecipe.getRequiredHeat().testBlazeBurner(highestHeatLevel))) {
                        currentRecipe = null;
                        processingTime = -1;
                    }
                }

                if (processingTime == 0 && currentRecipe != null) {
                    if (!level.isClientSide) {
                        level.playSound(null, getBlockPos().offset(width / 2, height / 2, width / 2),
                                SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.05f, 0.5f);
                        level.playSound(null, getBlockPos().offset(width / 2, height / 2, width / 2),
                                SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.05f, 0.5f);
                    }
                    if (currentRecipe.getRequiredHeat().testBlazeBurner(highestHeatLevel)) {
                        for (int j = 0; j < numberOfHighestHeat; j++) {
                            if (hasIngredients()) {
                                drainIngredients();
                            } else {
                                tanksFull = true;
                            }
                        }
                    }
                    currentRecipe = null;
                    processingTime = -1;
                    if (isController() && isBottom())
                        checkForRecipes();
                }

                progress = currentRecipe != null
                        ? (float) processingTime / currentRecipe.getProcessingDuration() : 0;
            }

            super.tick();

            if (tanksFull != prevTanksFull)
                sendData();
            if (syncCooldown > 0) {
                syncCooldown--;
                if (syncCooldown == 0 && queuedSync)
                    sendData();
            }
            if (lastKnownPos == null)
                lastKnownPos = getBlockPos();
            else if (!lastKnownPos.equals(worldPosition)) {
                onPositionChanged();
                return;
            }
            if (updateConnectivity)
                updateConnectivity();
            if (updateCapability) {
                updateCapability = false;
                refreshCapability();
            }
            if (fluidLevel != null)
                fluidLevel.tickChaser();
        }
        ```

        ### 7.3 Рецепт: проверка и применение (`CrackingRecipe.apply`)

        ```java
        public boolean apply(CrackingReactorBlockEntity be, boolean simulate) {
            IFluidHandler fluidCap = be.fluidCapability;
            if (!(fluidCap instanceof CrackingReactorBlockEntity.CrackingReactorFluidHandler availableFluids))
                return false;

            BlazeBurnerBlock.HeatLevel heat = be.highestHeatLevel;
            if (!getRequiredHeat().testBlazeBurner(heat))
                return false;

            for (boolean simulated : Iterate.trueAndFalse) {
                if (!simulated && simulate)
                    return true;

                int[] extractedFluidsFromTank = new int[availableFluids.getTanks()];

                // 1) проверка/дренаж ВХОДОВ из мультитанка
                FluidIngredients:
                for (SizedFluidIngredient fluidIngredient : getFluidIngredients()) {
                    int amountRequired = fluidIngredient.amount();
                    for (int tank = 0; tank < availableFluids.getTanks(); tank++) {
                        FluidStack fluidStack = availableFluids.getFluidInTank(tank);
                        if (simulated && fluidStack.getAmount() <= extractedFluidsFromTank[tank])
                            continue;
                        if (!fluidIngredient.test(fluidStack))
                            continue;
                        int drainedAmount = Math.min(amountRequired, fluidStack.getAmount());
                        if (!simulated)
                            fluidStack.shrink(drainedAmount);
                        amountRequired -= drainedAmount;
                        if (amountRequired != 0)
                            continue;
                        extractedFluidsFromTank[tank] += drainedAmount;
                        continue FluidIngredients;
                    }
                    return false;
                }

                if (!simulated)
                    be.onFluidStackChanged();

                // 2) распределение ВЫХОДОВ по блокам слоёв выше
                if (!applyOutputs(be, simulated))
                    return false;
            }
            return true;
        }
        ```

        ### 7.4 Ограничения рецепта

        ```java
        @Override protected int getMaxFluidInputCount() { return 2; }
        @Override protected int getMaxFluidOutputCount() { return 6; }
        @Override protected boolean canRequireHeat() { return true; }
        @Override protected boolean canSpecifyDuration() { return true; }
        ```

        ### 7.5 Тепло снизу (горелки)

        ```java
        public BlazeBurnerBlock.HeatLevel getHeat() {
            int width = getControllerBE().width;
            BlazeBurnerBlock.HeatLevel highestHeat = BlazeBurnerBlock.HeatLevel.NONE;
            for (int xOffset = 0; xOffset < width; xOffset++) {
                for (int zOffset = 0; zOffset < width; zOffset++) {
                    BlockPos pos = getController().offset(xOffset, -1, zOffset);
                    BlockState blockState = level.getBlockState(pos);
                    BlazeBurnerBlock.HeatLevel heat = BasinBlockEntity.getHeatLevelOf(blockState);
                    if (!highestHeat.isAtLeast(heat))
                        highestHeat = heat;
                }
            }
            return highestHeat;
        }
        ```

    === "Контроллер-предмет"

        ## 8. Контроллер-предмет

        `CrackingControllerItem` — ПКМ по **базовому блоку** (в нашем случае —
        бочке CDG `OilBarrelBlockEntity`) превращает структуру бочек в мультиблок.

        ```java
        @Override
        public InteractionResult useOn(UseOnContext context) {
            if (!(context.getLevel().getBlockEntity(context.getClickedPos())
                    instanceof OilBarrelBlockEntity obbe
                    && CDGBlocks.OIL_BARREL.has(obbe.getBlockState())))
                return super.useOn(context);

            ItemStack itemInHand = context.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
            BlockPos controllerPos = obbe.getController();
            int width = obbe.getControllerBE().getWidth();
            int height = obbe.getControllerBE().getHeight();

            // проверка минимальной высоты
            if (height < ModConfig.CRACKING_MIN_HEIGHT.get()) {
                if (context.getPlayer() instanceof ServerPlayer sp)
                    sp.connection.send(new ClientboundSetActionBarTextPacket(
                            Component.translatable("harderdiesel.actionbar.cracking_controller.too_short",
                                            ModConfig.CRACKING_MIN_HEIGHT.get())
                                    .withStyle(ChatFormatting.RED)));
                return InteractionResult.FAIL;
            }

            IFluidHandler tank = context.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, obbe.getBlockPos(), null);
            FluidStack fluidInTank = tank.getFluidInTank(0);
            List<BlockPos> positions = new ArrayList<>();

            for (int y = 0; y < height; y++) {
                for (int z = 0; z < width; z++) {
                    for (int x = 0; x < width; x++) {
                        if (positions.size() >= itemInHand.getCount() && !context.getPlayer().isCreative())
                            break;
                        BlockPos currentPos = controllerPos.offset(x, y, z);
                        if (ConnectivityHandler.isConnected(context.getLevel(), controllerPos, currentPos))
                            positions.add(currentPos);
                    }
                }
            }

            if (!context.getPlayer().isCreative() && width * width * height > itemInHand.getCount()) {
                return InteractionResult.FAIL;
            }

            for (BlockPos pos : positions) {
                context.getLevel().setBlock(pos, ModBlocks.CRACKING_REACTOR.get().defaultBlockState(), 3);
                if (context.getLevel().isClientSide) {
                    for (int i = 0; i < 30; i++) {
                        Vec3 offset = VecHelper.offsetRandomly(VecHelper.getCenterOf(pos), context.getLevel().getRandom(), .3f);
                        Vec3 motion = VecHelper.offsetRandomly(Vec3.ZERO, context.getLevel().getRandom(), .1f);
                        context.getLevel().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemInHand),
                                offset.x(), offset.y(), offset.z(), motion.x(), motion.y(), motion.z());
                    }
                }
            }

            AllSoundEvents.WRENCH_ROTATE.playAt(context.getLevel(),
                    controllerPos.getX() + (double) width / 2,
                    controllerPos.getY() + (double) height / 2,
                    controllerPos.getZ() + (double) width / 2, 2f, 1f, false);

            if (!context.getPlayer().isCreative() && !context.getLevel().isClientSide)
                itemInHand.shrink(positions.size());

            if (context.getLevel().getBlockEntity(controllerPos) instanceof CrackingReactorBlockEntity be) {
                be.updateConnectivity();
                be.updateVerticalMulti();
                be.updateTemperature();
                IFluidHandler distillerTank = context.getLevel()
                        .getCapability(Capabilities.FluidHandler.BLOCK, controllerPos, null);
                if (distillerTank != null)
                    distillerTank.fill(fluidInTank, IFluidHandler.FluidAction.EXECUTE);
            }

            return InteractionResult.SUCCESS;
        }
        ```

        Ключевые моменты:
        - читает `width`/`height` **мультиблока базовых блоков**;
        - конвертирует все позиции, объединённые `ConnectivityHandler.isConnected`;
        - после конвертации формирует мультиблок (`updateConnectivity`) и переносит
          жидкость.

    === "Синхронизация и NBT"

        ## 9. Синхронизация и NBT

        ### 9.1 `write`/`read`

        **Важно**: TankContent должен писаться/читаться для **всех** блоков (не
        только контроллера). Иначе клиент не увидит жидкость в слоях выхода.

        ```java
        @Override
        protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
            super.write(tag, registries, clientPacket);
            if (tanksFull)
                tag.putBoolean("TanksFull", true);
            if (updateConnectivity)
                tag.putBoolean("Uninitialized", true);
            if (lastKnownPos != null)
                tag.put("LastKnownPos", NbtUtils.writeBlockPos(lastKnownPos));
            if (!isController())
                tag.put("Controller", NbtUtils.writeBlockPos(controller));
            tag.put("TankContent", tankInventory.writeToNBT(registries, new CompoundTag()));
            if (isController()) {
                tag.putBoolean("Window", window);
                tag.putInt("Size", width);
                tag.putInt("Height", height);
                tag.putInt("Progress", processingTime);
            }
            tag.putInt("Luminosity", luminosity);
            if (!clientPacket)
                return;
            if (forceFluidLevelUpdate)
                tag.putBoolean("ForceFluidLevel", true);
            if (queuedSync)
                tag.putBoolean("LazySync", true);
            forceFluidLevelUpdate = false;
        }

        @Override
        protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
            super.read(tag, registries, clientPacket);
            BlockPos controllerBefore = controller;
            int prevSize = width;
            int prevHeight = height;
            tanksFull = tag.getBoolean("TanksFull");
            updateConnectivity = tag.contains("Uninitialized");
            luminosity = tag.getInt("Luminosity");
            controller = null;
            lastKnownPos = null;
            if (tag.contains("LastKnownPos"))
                lastKnownPos = NBTHelper.readBlockPos(tag, "LastKnownPos");
            if (tag.contains("Controller"))
                controller = NBTHelper.readBlockPos(tag, "Controller");

            if (isController()) {
                window = tag.getBoolean("Window");
                width = tag.getInt("Size");
                height = tag.getInt("Height");
                tankInventory.setCapacity(getTotalTankSize() * getCapacityMultiplier());
            }
            tankInventory.readFromNBT(registries, tag.getCompound("TankContent"));
            if (tag.contains("ForceFluidLevel") || fluidLevel == null)
                fluidLevel = LerpedFloat.linear().startWithValue(getFillState());

            updateCapability = true;
            if (!clientPacket)
                return;

            boolean changeOfController = !Objects.equals(controllerBefore, controller);
            if (changeOfController || prevSize != width || prevHeight != height) {
                if (hasLevel())
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
                if (isController())
                    tankInventory.setCapacity(getCapacityMultiplier() * getTotalTankSize());
                invalidateRenderBoundingBox();
            }
            if (isController()) {
                float fillState = getFillState();
                if (tag.contains("ForceFluidLevel") || fluidLevel == null)
                    fluidLevel = LerpedFloat.linear().startWithValue(fillState);
                fluidLevel.chase(fillState, 0.5f, LerpedFloat.Chaser.EXP);
                processingTime = tag.getInt("Progress");
            }
            if (tag.contains("LazySync"))
                fluidLevel.chase(fluidLevel.getChaseTarget(), 0.125f, LerpedFloat.Chaser.EXP);
            updateTemperature();
            List<Recipe<?>> r = getMatchingRecipes();
            if (!r.isEmpty()) {
                currentRecipe = (CrackingRecipe) r.get(0);
                if (processingTime <= 0)
                    startProcessing();
            }
        }
        ```

        ### 9.2 Плавность уровня (`LerpedFloat`) на клиенте

        ```java
        protected void tickClient() {
            IFluidHandler cap = level.getCapability(Capabilities.FluidHandler.BLOCK, worldPosition, null);
            if (cap instanceof CrackingReactorFluidHandler handler) {
                float fill = fillStateOf(handler);
                if (fluidLevel == null)
                    fluidLevel = LerpedFloat.linear().startWithValue(fill);
                fluidLevel.chase(fill, .5f, LerpedFloat.Chaser.EXP);
            }
        }
        ```

        `fluidLevel.tickChaser()` вызывается в `tick()`. Это даёт плавное поднятие
        уровня вместо резкого скачка.

    === "Клиент: модели и рендер"

        ## 10. Клиент: модели и рендер

        ### 10.1 Регистрация рендерера и модели (`ModClientEvents`)

        ```java
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(HarderDieselSpriteShifts::init);
            HarderDieselPartialModels.init();
            net.createmod.ponder.foundation.PonderIndex.addPlugin(new HarderDieselPonderPlugin());
            CreateClient.MODEL_SWAPPER.getCustomBlockModels()
                    .register(ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "cracking_reactor"),
                            model -> new CrackingReactorModel(model));
        }

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntityTypes.CRACKING_REACTOR.get(), CrackingReactorRenderer::new);
        }
        ```

        ### 10.2 SpriteShifts и CTBehavior

        ```java
        public class HarderDieselSpriteShifts {
            public static final CTSpriteShiftEntry CRACKING_REACTOR = rectangle("cracking_reactor/cracking_reactor"),
                    CRACKING_REACTOR_TOP = rectangle("cracking_reactor/cracking_reactor_top"),
                    CRACKING_REACTOR_NORTH = rectangle("cracking_reactor/cracking_reactor", "cracking_reactor/cracking_reactor_pipes_connected");

            public static void init() {}

            private static CTSpriteShiftEntry rectangle(String name, String connectedName) {
                return CTSpriteShifter.getCT(AllCTTypes.RECTANGLE,
                        ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "block/" + name),
                        ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "block/" + connectedName));
            }
        }
        ```

        ### 10.3 CTModel (отсечение внутренних граней)

        ```java
        public class CrackingReactorModel extends CTModel {
            protected static final ModelProperty<CullData> CULL_PROPERTY = new ModelProperty<>();

            public CrackingReactorModel(BakedModel originalModel) {
                super(originalModel, new CrackingReactorCTBehavior());
            }

            @Override
            protected ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world,
                    BlockPos pos, BlockState state, ModelData blockEntityData) {
                super.gatherModelData(builder, world, pos, state, blockEntityData);
                CullData cullData = new CullData();
                for (Direction d : Iterate.horizontalDirections)
                    cullData.setCulled(d, ConnectivityHandler.isConnected(world, pos, pos.relative(d)));
                return builder.with(CULL_PROPERTY, cullData);
            }
        }
        ```

        ### 10.4 Рендерер (жидкость + манометр)

        Модель блока — «полый куб»: стенки по краям (x/z 1–15, y 4–12). Рендерим
        жидкость внутри полости. **Манометр — только на контроллере нижнего
        слоя.**

        ```java
        public class CrackingReactorRenderer extends SafeBlockEntityRenderer<CrackingReactorBlockEntity> {

            @Override
            protected void renderSafe(CrackingReactorBlockEntity be, float partialTicks, PoseStack ms,
                                      MultiBufferSource buffer, int light, int overlay) {
                if (be.isController() && be.isBottom()) {
                    renderAsBoiler(be, partialTicks, ms, buffer, light, overlay);
                    return;
                }
                if (be.isBottom())
                    return;

                // Жидкость в блоках слоёв выхода
                if (be.getLevel() == null)
                    return;
                IFluidHandler fluids = be.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), null);
                if (!(fluids instanceof CrackingReactorBlockEntity.CrackingReactorFluidHandler handler))
                    return;

                FluidStack fluidStack = firstNonEmpty(handler);
                if (fluidStack.isEmpty())
                    return;

                LerpedFloat fluidLevel = be.getFluidLevel();
                float level = fluidLevel != null ? fluidLevel.getValue(partialTicks) : 0;
                level = Mth.clamp(level, 0, 1);

                float xMin = 1 / 16f, zMin = 1 / 16f;
                float xMax = 15 / 16f, zMax = 15 / 16f;
                float yMin = 4 / 16f;
                float yMax = yMin + level * (8 / 16f);
                if (yMax <= yMin)
                    return;

                ms.pushPose();
                NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(fluidStack, xMin, yMin, zMin, xMax, yMax, zMax,
                        buffer, ms, light, false, true);
                ms.popPose();
            }
        }
        ```

        Манометр (gauge) рисуется на контроллере через partial-модели и показывает
        прогресс рецепта (пример — `renderAsBoiler` с partial-моделью
        `CRACKING_REACTOR_GAUGE` и диалом `BOILER_GAUGE_DIAL`).

        ### 10.5 PartialModels

        ```java
        public class HarderDieselPartialModels {
            public static final PartialModel CRACKING_REACTOR_GAUGE = model("block/cracking_reactor/gauge");
            public static final PartialModel JEI_REACTOR_TOP = model("block/jei_reactor/top");
            public static final PartialModel JEI_REACTOR_MIDDLE = model("block/jei_reactor/middle");
            public static final PartialModel JEI_REACTOR_BOTTOM = model("block/jei_reactor/bottom");

            public static PartialModel model(String id) {
                return PartialModel.of(ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, id));
            }
            public static void init() {}
        }
        ```

    === "JEI + Ponder"

        ## 11. JEI + Ponder

        ### 11.1 JEI-плагин

        ```java
        @JeiPlugin
        public class HarderDieselJEI implements IModPlugin {
            private static final ResourceLocation ID =
                    ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "jei_plugin");
            private final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();

            @Override
            public ResourceLocation getPluginUid() { return ID; }

            private void loadCategories() {
                allCategories.clear();
                CreateRecipeCategory<CrackingRecipe> cracking =
                        new CreateRecipeCategory.Builder<>(CrackingRecipe.class)
                                .addTypedRecipes(ModRecipeTypes.CRACKING)
                                .catalyst(ModItems.CRACKING_CONTROLLER::get)
                                .doubleItemIcon(CDGBlocks.OIL_BARREL.get(), ModItems.CRACKING_CONTROLLER.get())
                                .emptyBackground(177, 200)
                                .build("cracking", CrackingCategory::new);
                allCategories.add(cracking);
            }

            @Override
            public void registerCategories(IRecipeCategoryRegistration registration) {
                loadCategories();
                registration.addRecipeCategories(allCategories.toArray(CreateRecipeCategory[]::new));
            }
            @Override
            public void registerRecipes(IRecipeRegistration registration) {
                allCategories.forEach(c -> c.registerRecipes(registration));
            }
            @Override
            public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
                allCategories.forEach(c -> c.registerCatalysts(registration));
            }
        }
        ```

        ### 11.2 Ponder

        ```java
        public class HarderDieselPonderPlugin implements PonderPlugin {
            @Override public String getModId() { return HarderDiesel.MODID; }

            @Override
            public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
                PonderPlugin.super.registerScenes(helper);
                helper.forComponents(ModItems.CRACKING_CONTROLLER.getId())
                        .addStoryBoard("cracking_reactor", CrackingReactorScene::scene);
            }
        }
        ```

        > Ponder-схематика `.nbt` лежит в `assets/<modid>/ponder/`. Она создаётся
        > отдельным генератором или структурным блоком.

    === "Ассеты и рецепты"

        ## 12. Ассеты

        ### 12.1 Блокстейт (`blockstates/cracking_reactor.json`)

        Варианты по `bottom`/`top`/`shape`:

        ```json
        {
          "variants": {
            "bottom=false,shape=plain,top=false": { "model": "harderdiesel:block/cracking_reactor/block" },
            "bottom=true,shape=plain,top=false": { "model": "harderdiesel:block/cracking_reactor/block_bottom" },
            "bottom=false,shape=window,top=false": { "model": "harderdiesel:block/cracking_reactor/block_window" }
          }
        }
        ```

        ### 12.2 Модель блока — полость

        `block.json` — «полый куб» (стенки 1px, полость x/z 1–15, y 4–12). Это
        используется рендерером для жидкости.

        ### 12.3 Lang

        **Важно**: `CreateLang.translate(...)` добавляет namespace `create`. Для
        своих ключей используйте `Lang.builder(MODID).translate(...)`:

        ```java
        Lang.builder(HarderDiesel.MODID).translate("goggles.inputs").forGoggles(tooltip);
        ```

        В `assets/<modid>/lang/ru_ru.json`:

        ```json
        {
          "item.harderdiesel.cracking_controller": "Контроллер крекинга",
          "block.harderdiesel.cracking_reactor": "Крекинг-реактор",
          "harderdiesel.actionbar.cracking_controller.too_short": "Реактор слишком низкий — нужно минимум %s блоков",
          "harderdiesel.hint.reactor_empty": "Пусто",
          "harderdiesel.hint.reactor_full.title": "Реактор заполнен"
        }
        ```

        ### 12.4 Loot table (`loot_table/blocks/cracking_reactor.json`)

        Мультиблок дропает базовые блоки + контроллер:

        ```json
        {
          "type": "minecraft:block",
          "pools": [
            {
              "rolls": 1,
              "bonus_rolls": 0,
              "entries": [ { "type": "minecraft:item", "name": "createdieselgenerators:oil_barrel" } ],
              "conditions": [ { "condition": "minecraft:survives_explosion" } ]
            },
            {
              "rolls": 1,
              "bonus_rolls": 0,
              "entries": [ { "type": "minecraft:item", "name": "harderdiesel:cracking_controller" } ],
              "conditions": [ { "condition": "minecraft:survives_explosion" } ]
            }
          ]
        }
        ```

        ### 12.5 JSON рецепта (2 входа)

        `data/<modid>/recipe/cracking/crude_oil_water_to_nitromethane.json`:

        ```json
        {
          "type": "harderdiesel:cracking",
          "ingredients": [
            { "type": "fluid_tag", "fluid_tag": "c:crude_oil", "amount": 100 },
            { "type": "neoforge:single", "fluid": "minecraft:water", "amount": 50 }
          ],
          "heat_requirement": "heated",
          "processing_time": 120,
          "results": [ { "id": "harderdiesel:nitromethane", "amount": 100 } ]
        }
        ```

        ### 12.6 Крафт контроллера

        `data/<modid>/recipe/crafting/cracking_controller.json`:

        ```json
        {
          "type": "minecraft:crafting_shaped",
          "pattern": ["PCP", "AIA"],
          "key": {
            "A": { "item": "create:andesite_alloy" },
            "I": { "tag": "c:plates/iron" },
            "C": { "item": "minecraft:clock" },
            "P": { "item": "create:fluid_pipe" }
          },
          "result": { "id": "harderdiesel:cracking_controller", "count": 4 }
        }
        ```

    === "Конфиг и ошибки"

        ## 13. Конфиг

        Минимальная высота/другие параметры — через `ModConfigSpec`:

        ```java
        public class ModConfig {
            public static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();
            public static final ModConfigSpec SERVER_SPEC;
            public static final ModConfigSpec.ConfigValue<Integer> CRACKING_MIN_HEIGHT;

            static {
                SERVER_BUILDER.push("cracking_reactor");
                CRACKING_MIN_HEIGHT = SERVER_BUILDER
                        .comment("Minimum height of the Cracking Reactor required to process recipes")
                        .defineInRange("Cracking Reactor Minimum Height", 3, 2, 7);
                SERVER_BUILDER.pop();
                SERVER_SPEC = SERVER_BUILDER.build();
            }

            public static void register(ModContainer container) {
                container.registerConfig(net.neoforged.fml.config.ModConfig.Type.SERVER,
                        SERVER_SPEC, HarderDiesel.MODID + "-server.toml");
            }
        }
        ```

        ## 14. Подводные камни и частые ошибки

        Ниже — ошибки, с которыми мы реально столкнулись при создании
        Крекинг-реактора, и их решения.

        1. **NPE `this.level is null` в конструкторе BE.** `SmartBlockEntity`
           ставит `level` после конструктора, а `refreshCapability()` вызывается
           в конструкторе. Если `handlerForCapability()` трогает `level` — краш.
           **Решение**: `if (level == null) return tankInventory;` в начале.

        2. **`CreateLang.translate` добавляет namespace `create`.**
           `CreateLang.builder()` создаёт `LangBuilder` с `Create.ID`. Поэтому
           ключ `create.harderdiesel.goggles.inputs` — не находится. **Решение**:
           `Lang.builder(HarderDiesel.MODID).translate("goggles.inputs")`.

        3. **TankContent пишется/читается только для контроллера.** Блоки выше не
           сериализовали жидкость → клиент не видел выходы. **Решение**: писать/
           читать TankContent для **всех** блоков.

        4. **Манометр/рендер на всех блоках.** Манометр должен быть только на
           нижнем контроллере. **Решение**: `if (be.isController() &&
           be.isBottom()) renderAsBoiler(...)`.

        5. **Create-труба работает через капабилити каждого блока.**
           `handlerForCapability()` должен делегировать: нижний слой → контроллер,
           слои выхода → представитель слоя.

        6. **Ёмкость по площади, а не по высоте.** `getTotalTankSize()` =
           `width * width`; `applyFluidTankSize` должен использовать площадь.

        7. **`getMaxLength(Y, width) = 1`.** `formMulti` объединяет только один
           горизонтальный слой. Вертикальность — через `updateVerticalMulti`,
           `isBottom`, `isSameMultiBlock`.

        8. **Дюп манометров при ширине >1.** Рендерить gauge только если
           `isController()` (среди нижних только один).

        9. **Self-reference в DeferredRegister BE-типа.** Нельзя ссылаться на
           `TYPE` напрямую в лямбде. **Решение**: вынести создание в отдельные
           статические методы.

        10. **Локальные jar-зависимости (catnip/ponder/flywheel/Registrate).**
            В dev-окружении Create не кладёт их на compile classpath. Добавьте
            через `compileOnly`.

        11. **Не забыть `neoforge.mods.toml`.** Зависимость от Create обязательна;
            CDG — как источник.

        12. **`isRedstoneConductor` + `noOcclusion`.** Блок должен корректно
            отдавать свет и не пропускать через себя.

    === "Чек-лист и быстрый старт"

        ## 15. Чек-лист добавления нового мультиблока

        - [ ] 1. Определить базовый блок (что игрок строит до активации).
        - [ ] 2. Создать рецепт-тип: `<Machine>Recipe.java` + enum в `ModRecipeTypes`.
        - [ ] 3. Создать `<Machine>Block.java` (IBE, IWrenchable, TOP/BOTTOM/SHAPE).
        - [ ] 4. Создать `<Machine>BlockEntity.java` (контроллер, размеры, tick, капабилити).
        - [ ] 5. Зарегистрировать BE-тип в `ModBlockEntityTypes` (обойти self-reference).
        - [ ] 6. Зарегистрировать блок в `ModBlocks` и капабилити в `HarderDiesel`.
        - [ ] 7. Создать `<Machine>ControllerItem.java` и зарегистрировать в `ModItems`.
        - [ ] 8. Реализовать `handlerForCapability` и мультитанк (если нужно несколько жидкостей).
        - [ ] 9. Синхронизация: TankContent для всех блоков, `fluidLevel` (LerpedFloat).
        - [ ] 10. Клиент: SpriteShifts, CTBehavior, Model (CTModel), Renderer, PartialModels.
        - [ ] 11. Зарегистрировать рендерер и модель в `ModClientEvents`.
        - [ ] 12. JEI-категория (`CrackingCategory` + плагин).
        - [ ] 13. Ponder-сцена + `.nbt` схематика + плагин.
        - [ ] 14. Ассеты: блокстейт, модели, item-модели, lang, loot, JSON рецепта, крафт.
        - [ ] 15. `gradlew build` и `runClient`-тест: сборка мультиблока, вход/выход жидкостей.

        ## Быстрый старт: копирование из CDG

        Самый надёжный путь для новой машины — скопировать реализацию из
        **Create: Diesel Generators** (репозиторий
        `george8188625/Create-Diesel-Generators`, ветка `1.21.1`) и адаптировать:

        1. Скопируйте файлы из `content/<machine>/` (Block, BlockEntity, ControllerItem, Recipe).
        2. Переименуйте классы и пакеты под свой мод.
        3. Замените регистрацию с Registrate на `DeferredRegister` (см. раздел «Регистрация»).
        4. Обновите типы рецептов (`CDGRecipes` → `ModRecipeTypes`).
        5. Обновите ассеты (текстуры, модели, lang) под свой неймспейс.
        6. Протестируйте в `runClient` и исправьте подводные камни из раздела «Конфиг и ошибки».

        **Ключевые пути в CDG (образцы):**

        - `content/distillation/` — колонна (ближайший аналог мультиблока-колонны);
        - `content/oil_barrel/` — бочка (базовый блок с мультитанком);
        - `content/bulk_fermenter/` — ферментер (мультитанк, heat, рецепты);
        - `content/burner/` — горелка (источник тепла).
