# Пособие: создание Create-мультиблоков (NeoForge 1.21.1)

> Практическое руководство по созданию мультиблоков на базе Create, написанное
> по мотивам реальной реализации **Крекинг-реактора** в моде
> `harderdiesel` — порта «Ректификационной колонны» (Distillation Tower) из
> мода **Create: Diesel Generators** (CDG, `createdieselgenerators`).

---

## Оглавление

1. [Введение](#1-введение)
2. [Общая архитектура](#2-общая-архитектура)
3. [Регистрация](#3-регистрация)
4. [Блок](#4-блок)
5. [Блок-сущность: ядро мультиблока](#5-блок-сущность-ядро-мультиблока)
6. [Капабилити жидкости](#6-капабилити-жидкости)
7. [Обработка рецептов](#7-обработка-рецептов)
8. [Контроллер-предмет](#8-контроллер-предмет)
9. [Синхронизация и NBT](#9-синхронизация-и-nbt)
10. [Клиент: модели и рендер](#10-клиент-модели-и-рендер)
11. [JEI + Ponder](#11-jei--ponder)
12. [Ассеты](#12-ассеты)
13. [Конфиг](#13-конфиг)
14. [Подводные камни и частые ошибки](#14-подводные-камни-и-частые-ошибки)
15. [Чек-лист добавления нового мультиблока](#15-чек-лист-добавления-нового-мультиблока)

---

## 1. Введение

Мультиблок в Create — это группа блоков, которые объединяются в одну машину
через систему `ConnectivityHandler`. Типичный пример — жидкостный бак
(`FluidTank`), ректификационная колонна CDG, ферментер. Один блок становится
**контроллером**, остальные — **частями**; логика обрабатывается только в
контроллере, а части делегируют к нему.

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
| `src/main/java/com/harderdiesel/content/cracking/CrackingReactorBlock.java` | `content/distillation/DistillationTankBlock.java` |
| `src/main/java/com/harderdiesel/content/cracking/CrackingReactorBlockEntity.java` | `content/distillation/DistillationTankBlockEntity.java` |
| `src/main/java/com/harderdiesel/content/cracking/CrackingControllerItem.java` | `content/distillation/DistillationControllerItem.java` |
| `src/main/java/com/harderdiesel/content/cracking/CrackingRecipe.java` | `content/distillation/DistillationRecipe.java` |
| `ModRecipeTypes.java` | `CDGRecipes.java` |
| `ModBlockEntityTypes.java` | `CDGBlockEntityTypes.java` |

> Совет: при добавлении новых машин проще всего **копировать реализацию CDG**
> и адаптировать. Источник: `https://github.com/george8188625/Create-Diesel-Generators` (ветка `1.21.1`).

---

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

---

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

> В `CrackingRecipe` конструктор принимает `ProcessingRecipeParams` и вызывает
> `super(ModRecipeTypes.CRACKING, params)` — рецепт знает свой тип через enum.

### 3.2 Тип блок-сущности (`ModBlockEntityTypes.java`)

> **Важно**: внутри лямбды `register(...)` нельзя ссылаться на сам
> `DeferredHolder` напрямую (self-reference). Решение — отдельные статические
> методы, вызываемые после регистрации.

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

> Порядок регистрации в конструкторе мода важен: предметы/блоки/жидкости →
> рецепты → BE-типы → конфиг.

---

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
    // ...
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
- `onPlace` → `updateConnectivity()` (формирует мультиблок) + `updateVerticalMulti()` (TOP/BOTTOM).
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

> Для рендера окон/стенок (`SHAPE`) нужны `mirror`/`rotate`, чтобы правильно
> отображать `WINDOW_*` варианты при поворотах схем/механизмов.

---

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
    // ...
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

@Override
public BlockPos getController() {
    return isController() ? worldPosition : controller;
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

@Override
public int getHeight() { return height; }
@Override
public void setHeight(int height) { this.height = height; }
@Override
public int getWidth() { return width; }
@Override
public void setWidth(int width) { this.width = width; }
```

> **Критично**: при `getMaxLength(Y,width)=1` `ConnectivityHandler.formMulti`
> объединяет только один **горизонтальный** слой (например, 2×2 или 3×3).
> Вертикальность (слои выше) реализуется отдельно через `updateVerticalMulti`
> и `isSameMultiBlock`.

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

boolean isSameMultiBlock(CrackingReactorBlockEntity be) {
    CrackingReactorBlockEntity otherControllerBE = be.getControllerBE();
    CrackingReactorBlockEntity controllerBE = getControllerBE();
    if (otherControllerBE == null || controllerBE == null)
        return false;
    if (otherControllerBE == controllerBE)
        return true;
    if (otherControllerBE.getBlockPos().getX() == controllerBE.getBlockPos().getX()
            && otherControllerBE.getBlockPos().getZ() == controllerBE.getBlockPos().getZ())
        return otherControllerBE.width == controllerBE.width;
    return false;
}
```

- `updateVerticalMulti` рекурсивно идёт вниз и ставит `TOP`/`BOTTOM` на каждый блок.
- `isBottom()` — истина для всех блоков нижнего слоя (под ними нет реактора того же мультиблока).
- `isSameMultiBlock` — проверяет, что два блока принадлежат одной колонне
  (одинаковые X/Z и ширина).

`notifyMultiUpdated` — вызывается после `formMulti`/`setWidth`, пересчитывает
ё мкость и состояния:

```java
@Override
public void notifyMultiUpdated() {
    BlockState state = this.getBlockState();
    if (CrackingReactorBlock.isReactor(state)) {
        state = state.setValue(CrackingReactorBlock.BOTTOM, getBottomConnectivity());
        state = state.setValue(CrackingReactorBlock.TOP, getTopConnectivity());
        level.setBlock(getBlockPos(), state, 6);
    }
    if (isController()) {
        applyFluidTankSize(1);
        setWindows(window);
    }
    onFluidStackChanged(tankInventory.getFluid());
    setChanged();
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

---

## 6. Капабилити жидкости

### 6.1 Распределение по слоям (`handlerForCapability`)

Create-труба/помпа работает через `Capabilities.FluidHandler.BLOCK` **каждого
блока**. Чтобы входы попадали в контроллер из любого нижнего блока, а выходы
выкачивались из любого блока слоя, капабилити делегирует:

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

> `level == null` в начале — защита от NPE при создании BE (в конструкторе
> `level` ещё не установлен).

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
        // сначала — танк с той же жидкостью, затем — пустой
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

    @Override
    public CompoundTag writeToNBT(HolderLookup.Provider registries, CompoundTag compound) {
        ListTag list = new ListTag();
        for (FluidTank tank : tanks)
            list.add(tank.writeToNBT(registries, new CompoundTag()));
        compound.put("Tanks", list);
        return compound;
    }

    @Override
    public FluidTank readFromNBT(HolderLookup.Provider registries, CompoundTag tag) {
        ListTag list = tag.getList("Tanks", Tag.TAG_COMPOUND);
        for (int i = 0; i < tanks.size() && i < list.size(); i++)
            tanks.get(i).readFromNBT(registries, list.getCompound(i));
        return this;
    }

    @Override
    public FluidTank setCapacity(int capacity) {
        for (FluidTank tank : tanks)
            tank.setCapacity(capacity);
        return super.setCapacity(capacity);
    }
}
```

> Образец: `BulkFermenterFluidHandler` в CDG (та же идея, 6 танков).

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

### 6.4 Методы интерфейса `IMultiBlockEntityContainer.Fluid`

```java
@Override public boolean hasTank() { return true; }
@Override public int getTankSize(int tank) { return getCapacityMultiplier(); }
@Override public void setTankSize(int tank, int blocks) { applyFluidTankSize(blocks); }
@Override public IFluidTank getTank(int tank) {
    return tank < tankInventory.getTanks() ? tankInventory.tanks.get(tank) : tankInventory;
}
@Override public FluidStack getFluid(int tank) {
    return tankInventory.getFluidInTank(tank).copy();
}
```

---

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

    if (level.isClientSide)
        CatnipServices.PLATFORM.executeOnClientOnly(() -> this::tickClient);
}
```

> `hasIngredients()`/`drainIngredients()` делегируют в `currentRecipe.apply(this, true/false)`.

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

private boolean applyOutputs(CrackingReactorBlockEntity be, boolean simulate) {
    int i = 0;
    for (FluidStack fluidResult : getFluidResults()) {
        if (fluidResult.isEmpty()) { i++; continue; }
        BlockEntity target = be.getLevel().getBlockEntity(be.getBlockPos().above(i + 1));
        if (!(target instanceof CrackingReactorBlockEntity outBe))
            return false;
        if (!be.isSameMultiBlock(outBe))
            return false;

        if (simulate) {
            int filled = outBe.tankInventory.fill(fluidResult.copy(), IFluidHandler.FluidAction.SIMULATE);
            if (filled < fluidResult.getAmount())
                return false;
        } else {
            outBe.tankInventory.fill(fluidResult.copy(), IFluidHandler.FluidAction.EXECUTE);
        }
        i++;
    }
    return true;
}
```

> Двухпроходный цикл `Iterate.trueAndFalse`: первый проход — проверка (simulate),
> второй — фактическое применение. При `simulate=true` внешний цикл останавливается
> на втором проходе и возвращает `true`.

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

> `BasinBlockEntity.getHeatLevelOf` понимает Blaze Burner и горелки CDG.
> `updateTemperature()` вызывается из `initialize()` и `updateShape(DOWN)`.

---

## 8. Контроллер-предмет

`CrackingControllerItem` — ПКМ по **базовому блоку** (в нашем случае — бочке
CDG `OilBarrelBlockEntity`) превращает структуру бочек в мультиблок.

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
        // не хватает предметов
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
- после конвертации формирует мультиблок (`updateConnectivity`) и переносит жидкость.

---

## 9. Синхронизация и NBT

### 9.1 `write`/`read`

> **Важно**: TankContent должен писаться/читаться для **всех** блоков
> (не только контроллера). Иначе клиент не увидит жидкость в слоях выхода.

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

protected float fillStateOf(CrackingReactorFluidHandler handler) {
    long amount = 0, capacity = 0;
    for (int i = 0; i < handler.getTanks(); i++) {
        amount += handler.getFluidInTank(i).getAmount();
        capacity += handler.getTankCapacity(i);
    }
    return capacity == 0 ? 0 : (float) amount / capacity;
}
```

> `fluidLevel.tickChaser()` вызывается в `tick()`. Это даёт плавное поднятие
> уровня вместо резкого скачка.

---

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

```java
public class CrackingReactorCTBehavior extends ConnectedTextureBehaviour.Base {
    @Override
    public CTSpriteShiftEntry getShift(BlockState state, Direction direction, TextureAtlasSprite sprite) {
        if (direction.getAxis().isVertical())
            return HarderDieselSpriteShifts.CRACKING_REACTOR_TOP;
        if (direction == Direction.NORTH)
            return HarderDieselSpriteShifts.CRACKING_REACTOR_NORTH;
        return HarderDieselSpriteShifts.CRACKING_REACTOR;
    }

    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader,
                              BlockPos pos, BlockPos otherPos, Direction face,
                              Direction primaryOffset, Direction secondaryOffset) {
        if (pos.above(1).equals(otherPos))
            return !state.getValue(CrackingReactorBlock.TOP);
        if (pos.below(1).equals(otherPos))
            return !state.getValue(CrackingReactorBlock.BOTTOM);
        return other.getBlock() instanceof CrackingReactorBlock
                && ConnectivityHandler.isConnected(reader, pos, otherPos);
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

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand,
                                    ModelData extraData, RenderType renderType) {
        if (side != null)
            return Collections.emptyList();
        List<BakedQuad> quads = new ArrayList<>();
        for (Direction d : Iterate.directions) {
            if (extraData.has(CULL_PROPERTY) && extraData.get(CULL_PROPERTY).isCulled(d))
                continue;
            quads.addAll(super.getQuads(state, d, rand, extraData, renderType));
        }
        quads.addAll(super.getQuads(state, null, rand, extraData, renderType));
        return quads;
    }
}
```

### 10.4 Рендерер (жидкость + манометр)

Модель блока — «полый куб»: стенки по краям (x/z 1–15, y 4–12). Рендерим
жидкость внутри полости. **Манометр — только на контроллере нижнего слоя.**

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

    private FluidStack firstNonEmpty(CrackingReactorBlockEntity.CrackingReactorFluidHandler handler) {
        for (int i = 0; i < handler.getTanks(); i++) {
            FluidStack stack = handler.getFluidInTank(i);
            if (!stack.isEmpty())
                return stack;
        }
        return FluidStack.EMPTY;
    }
}
```

Манометр (gauge) рисуется на контроллере через partial-модели и показывает
прогресс рецепта:

```java
protected void renderAsBoiler(CrackingReactorBlockEntity be, float partialTicks, PoseStack ms,
                              MultiBufferSource buffer, int light, int overlay) {
    BlockState blockState = be.getBlockState();
    VertexConsumer vb = buffer.getBuffer(RenderType.cutout());
    ms.pushPose();
    TransformStack msr = TransformStack.of(ms);
    msr.translate(be.getWidth() / 2f, 0.5, be.getWidth() / 2f);

    float dialPivotY = 6f / 16;
    float dialPivotZ = 8f / 16;
    float progress = Mth.clamp(be.currentRecipe == null ? be.progress
            : (be.processingTime - partialTicks) / be.currentRecipe.getProcessingDuration(), 0, 1);

    for (Direction d : Iterate.horizontalDirections) {
        ms.pushPose();
        CachedBuffers.partial(HarderDieselPartialModels.CRACKING_REACTOR_GAUGE, blockState)
                .rotateYDegrees(d.toYRot()).uncenter()
                .translate(be.getWidth() / 2f - 6 / 16f, 0, 0)
                .light(light).renderInto(ms, vb);
        CachedBuffers.partial(AllPartialModels.BOILER_GAUGE_DIAL, blockState)
                .rotateYDegrees(d.toYRot()).uncenter()
                .translate(be.width / 2f - 6 / 16f, 0, 0)
                .translate(0, dialPivotY, dialPivotZ)
                .rotateXDegrees(-145 * progress + 90)
                .translate(0, -dialPivotY, -dialPivotZ)
                .light(light).renderInto(ms, vb);
        ms.popPose();
    }
    ms.popPose();
}
```

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

> PartialModel-файлы лежат в `assets/<modid>/models/block/...` и используются в
> рендере (gauge) и JEI-анимации.

---

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

Категория рисует два входа и выходы по слоям:

```java
public class CrackingCategory extends CreateRecipeCategory<CrackingRecipe> {
    private final AnimatedCrackingReactor reactor = new AnimatedCrackingReactor();
    private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();

    public CrackingCategory(Info<CrackingRecipe> info) { super(info); }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CrackingRecipe recipe, IFocusGroup focuses) {
        if (recipe.getFluidIngredients().isEmpty())
            return;
        addFluidSlot(builder, 17, 145, recipe.getFluidIngredients().get(0));
        if (recipe.getFluidIngredients().size() > 1)
            addFluidSlot(builder, 40, 145, recipe.getFluidIngredients().get(1));

        int i = 1;
        for (FluidStack fluidResult : recipe.getFluidResults()) {
            addFluidSlot(builder, 130, -23 * i + 150, fluidResult);
            i++;
        }
        // ... иконки Blaze Burner / Blaze Cake по требованию тепла
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

Сцена использует `util.select().fromTo(...)` для выбора зон и `showIndependentSection`:

```java
public static void scene(SceneBuilder builder, SceneBuildingUtil util) {
    CreateSceneBuilder scene = new CreateSceneBuilder(builder);
    scene.title("cracking_reactor", "Setting up a Cracking Reactor");
    scene.configureBasePlate(1, 0, 5);
    scene.showBasePlate();

    Selection reactor = util.select().fromTo(3, 2, 2, 4, 4, 3);
    Selection barrels = util.select().fromTo(3, 2, 0, 4, 4, 1);
    // ...
    scene.overlay().showText(70).attachKeyFrame()
            .text("Apply a Cracking Controller to ...")
            .colored(PonderPalette.BLUE)
            .pointAt(util.vector().topOf(2, 2, 2))
            .placeNearTarget();
    // ...
}
```

> Ponder-схематика `.nbt` лежит в `assets/<modid>/ponder/`. Она создаётся
> отдельным генератором (пример — `gen_ponder_nbt.py` в этой разработке) или
> структурным блоком.

---

## 12. Ассеты

### 12.1 Блокстейт (`assets/<modid>/blockstates/cracking_reactor.json`)

Варианты по `bottom`/`top`/`shape`. Для нижних блоков — модель `block_bottom`,
для остальных — `block`, `block_window*`:

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
используется рендерером для жидкости:

```json
{
  "credit": "Made with Blockbench",
  "parent": "block/block",
  "render_type": "cutout",
  "textures": {
    "0": "harderdiesel:block/cracking_reactor/cracking_reactor_top",
    "1": "harderdiesel:block/cracking_reactor/cracking_reactor",
    "particle": "harderdiesel:block/cracking_reactor/cracking_reactor"
  },
  "elements": [
    { "from": [0, 0, 0], "to": [16, 4, 16] },
    { "from": [15, 4, 0], "to": [16, 12, 16] },
    { "from": [0, 4, 15], "to": [16, 12, 16] },
    { "from": [0, 4, 0], "to": [1, 12, 16] },
    { "from": [0, 12, 0], "to": [16, 16, 16] },
    { "from": [0, 4, 0], "to": [16, 12, 1] }
  ]
}
```

### 12.3 Item-модель контроллера

```json
{
  "parent": "minecraft:item/generated",
  "textures": { "layer0": "harderdiesel:item/cracking_controller" }
}
```

### 12.4 Lang

> **Важно**: `CreateLang.translate(...)` добавляет namespace `create`.
> Для своих ключей используйте `Lang.builder(MODID).translate(...)`:

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
  "harderdiesel.hint.reactor_full.title": "Реактор заполнен",
  "harderdiesel.hint.reactor_full": "Освободите выходные баки, чтобы реактор продолжил работу"
}
```

### 12.5 Loot table (`data/<modid>/loot_table/blocks/cracking_reactor.json`)

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

### 12.6 JSON рецепта (2 входа)

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

### 12.7 Крафт контроллера

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

---

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

---

## 14. Подводные камни и частые ошибки

Ниже — ошибки, с которыми мы реально столкнулись при создании
Крекинг-реактора, и их решения.

1. **NPE `this.level is null` в конструкторе BE**
   `SmartBlockEntity` ставит `level` после конструктора, а `refreshCapability()`
   вызывается в конструкторе. Если `handlerForCapability()` трогает `level` —
   краш.
   **Решение**: `if (level == null) return tankInventory;` в начале.

2. **`CreateLang.translate` добавляет namespace `create`**
   `CreateLang.builder()` создаёт `LangBuilder` с `Create.ID`. Поэтому
   `CreateLang.translate("harderdiesel.goggles.inputs")` → ключ
   `create.harderdiesel.goggles.inputs` — не находится, показывается fallback.
   **Решение**: `Lang.builder(HarderDiesel.MODID).translate("goggles.inputs")`.

3. **TankContent пишется/читается только для контроллера**
   В `write()`/`read()` блоки выше не сериализовали жидкость → клиент не видел
   выходы («плейсхолдеры» вместо уровней).
   **Решение**: писать/читать TankContent для **всех** блоков.

4. **Манометр/рендер на всех блоках**
   `renderSafe` вызывается для каждого BE. Манометр (gauge) должен быть только
   на нижнем контроллере.
   **Решение**: `if (be.isController() && be.isBottom()) renderAsBoiler(...)`.

5. **Create-труба работает через капабилити каждого блока**
   Чтобы входы шли в контроллер, а выходы выкачивались из любого блока слоя,
   `handlerForCapability()` должен делегировать: нижний слой → контроллер,
   слои выхода → представитель слоя.

6. **Ёмкость по площади, а не по высоте**
   `getTotalTankSize()` = `width * width`; `applyFluidTankSize` должен
   использовать площадь, иначе колонна разной высоты даёт разную ёмкость
   на один танк.

7. **`getMaxLength(Y, width) = 1`**
   `formMulti` объединяет только один горизонтальный слой. Вертикальные слои
   связываются через `updateVerticalMulti`, `notifyMultiUpdated`, `isBottom`,
   `isSameMultiBlock`. Не пытайтесь получить вертикальную связность только из
   `formMulti`.

8. **Дюп манометров при ширине >1**
   Нижний слой 2×2 имеет 4 блока с `isBottom()==true`. Рендерить gauge
   только если `isController()` (среди нижних только один).

9. **Self-reference в DeferredRegister BE-типа**
   Внутри `BLOCK_ENTITY_TYPES.register("x", () -> new BE(TYPE.get(), ...))`
   нельзя ссылаться на `TYPE` напрямую. **Решение**: вынести создание в
   отдельные статические методы (`createReactorType`/`createReactor`).

10. **Локальные jar-зависимости (catnip/ponder/flywheel/Registrate)**
    В dev-окружении Create не кладёт их на compile classpath. Добавьте их в
    `build.gradle` через `compileOnly` (flatDir `../harderdiesel-build/libs`) —
    см. начало `build.gradle` этого проекта.

11. **Не забыть `neoforge.mods.toml`**
    Зависимость от Create обязательна; CDG — как источник. Версия и range —
    под версию Create.

12. **`isRedstoneConductor` + `noOcclusion`**
    Блок должен корректно отдавать свет и не пропускать через себя; без
    `isRedstoneConductor((p1,p2,p3) -> true)` могут быть проблемы со светом.

---

## 15. Чек-лист добавления нового мультиблока

По порядку, каждый шаг проверяется сборкой.

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
- [ ] 15. `gradlew build` и `runClient`-тест: сборка мультиблока, вход/выход
      жидкостей, подсказки, рендер.

---

## Быстрый старт: копирование из CDG

Самый надёжный путь для новой машины — скопировать реализацию из
**Create: Diesel Generators** (репозиторий
`george8188625/Create-Diesel-Generators`, ветка `1.21.1`) и адаптировать:

1. Скопируйте файлы из `content/<machine>/` (Block, BlockEntity, ControllerItem, Recipe).
2. Переименуйте классы и пакеты под свой мод.
3. Замените регистрацию с Registrate на `DeferredRegister` (см. раздел 3).
4. Обновите типы рецептов (`CDGRecipes` → `ModRecipeTypes`).
5. Обновите ассеты (текстуры, модели, lang) под свой неймспейс.
6. Протестируйте в `runClient` и исправьте подводные камни из раздела 14.

**Ключевые пути в CDG (образцы):**

- `content/distillation/` — колонна (ближайший аналог мультиблока-колонны);
- `content/oil_barrel/` — бочка (базовый блок с мультитанком);
- `content/bulk_fermenter/` — ферментер (мультитанк, heat, рецепты);
- `content/burner/` — горелка (источник тепла).
