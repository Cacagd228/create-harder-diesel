# Руководство по интеграции с XaerosZones Pollution API
> **Платформа:** Minecraft 1.21.1 | **Загрузчик:** NeoForge | **Маппинги:** Mojmap

XaerosZones предоставляет расширяемый **Pollution API** для визуализации смога, радиации или любого другого загрязнения на **Xaero's World Map** и **Xaero's Minimap** в почанковом стиле **Factorio** (или в режиме сглаженного органического облака).

---

## 1. Архитектура системы

```
┌─────────────────────────────────────────────────────────────┐
│                    Сторонний мод (Ваш мод)                  │
│                                                             │
│   Вариант А: IPollutionProvider                             │
│   Вариант Б: Пакетная синхронизация (Server -> Client)      │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│          com.slavav.xaeroszones.api.pollution               │
│   • IPollutionProvider      — интерфейс поставщика данных   │
│   • PollutionRegistry       — глобальный реестр             │
│   • PollutionColorGradient  — цветовая палитра и градиенты  │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│    ClientPollutionData ──► PollutionRenderer (Factorio GUI) │
│       (Кеш чанков)          (Карта мира + Миникарта)        │
└─────────────────────────────────────────────────────────────┘
```

---

## 2. Способ 1: Регистрация провайдера (`IPollutionProvider`)

Этот способ идеально подходит, если ваш мод хранит или рассчитывает данные о загрязнении на клиенте.

### Шаг 1. Реализация интерфейса
Создайте класс, реализующий `IPollutionProvider`:

```java
package com.example.mymod.pollution;

import com.slavav.xaeroszones.api.pollution.IPollutionProvider;
import com.slavav.xaeroszones.api.pollution.PollutionColorGradient;

public class MyModPollutionProvider implements IPollutionProvider {

    @Override
    public float getPollution(String dimensionId, int chunkX, int chunkZ) {
        // Возвращает уровень загрязнения чанка (>= 0.0F)
        // chunkX = blockX >> 4, chunkZ = blockZ >> 4
        return MyPollutionManager.getChunkPollution(dimensionId, chunkX, chunkZ);
    }

    @Override
    public PollutionColorGradient getGradient() {
        // Выбор цветовой шкалы: FACTORIO_RED, TOXIC_GREEN, SMOG_GRAY или кастомная
        return PollutionColorGradient.FACTORIO_RED;
    }

    @Override
    public float getVisibilityThreshold() {
        // Минимальный уровень загрязнения для начала отображения (по умолчанию 1.0F)
        return 1.0F;
    }

    @Override
    public float getMaxPollutionReference() {
        // Значение загрязнения, соответствующее 100% максимальной плотности цвета
        return 1000.0F;
    }

    @Override
    public boolean isEnabled() {
        // Можно динамически включать/выключать поставщик
        return true;
    }
}
```

### Шаг 2. Регистрация в реестре
Зарегистрируйте провайдер при инициализации клиентской части мода:

```java
import com.slavav.xaeroszones.api.pollution.PollutionRegistry;
import net.minecraft.resources.ResourceLocation;

public class MyModClient {
    public static void onClientSetup() {
        PollutionRegistry.registerProvider(
            ResourceLocation.fromNamespaceAndPath("mymod", "pollution_provider"),
            new MyModPollutionProvider()
        );
    }
}
```

---

## 3. Способ 2: Сетевая синхронизация с сервера (Server $\rightarrow$ Client)

Если ваша симуляция загрязнения работает на выделенном сервере, XaerosZones предоставляет готовые сетевые пакеты для отправки данных клиентам без написания собственного сетевого слоя.

### А. Обновление отдельного чанка
Когда чанк загрязняется или очищается:

```java
import com.slavav.xaeroszones.network.packet.s2c.SyncPollutionChunkPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

// Отправка конкретному игроку:
PacketDistributor.sendToPlayer(player, 
    new SyncPollutionChunkPacket("minecraft:overworld", chunkX, chunkZ, 450.0F));

// Отправка всем игрокам в измерении:
PacketDistributor.sendToPlayersInDimension(serverLevel, 
    new SyncPollutionChunkPacket("minecraft:overworld", chunkX, chunkZ, 450.0F));
```

### Б. Пакетная синхронизация области (Batch Area Sync)
При входе игрока в мир или смене измерения:

```java
import com.slavav.xaeroszones.network.packet.s2c.SyncPollutionAreaPacket;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.network.PacketDistributor;
import java.util.Map;
import java.util.HashMap;

Map<ChunkPos, Float> areaData = new HashMap<>();
areaData.put(new ChunkPos(chunkX, chunkZ), 800.0F);
areaData.put(new ChunkPos(chunkX + 1, chunkZ), 650.0F);
// ... заполнение чанков вокруг игрока

PacketDistributor.sendToPlayer(player, 
    new SyncPollutionAreaPacket("minecraft:overworld", areaData));
```

### В. Сброс / Очистка данных
```java
import com.slavav.xaeroszones.network.packet.s2c.ClearPollutionPacket;
import net.neoforged.neoforge.network.PacketDistributor;

// Очистить конкретное измерение:
PacketDistributor.sendToPlayer(player, new ClearPollutionPacket("minecraft:overworld"));

// Очистить все измерения:
PacketDistributor.sendToPlayer(player, new ClearPollutionPacket("*"));
```

---

## 4. Кастомизация цветовых градиентов (`PollutionColorGradient`)

Вы можете использовать встроенные палитры или задать собственный градиент с произвольным количеством контрольных точек (ColorStops). Цвета задаются в формате `0xAARRGGBB`:

```java
import com.slavav.xaeroszones.api.pollution.PollutionColorGradient;
import java.util.List;

// 1. Встроенные палитры:
PollutionColorGradient red = PollutionColorGradient.FACTORIO_RED; // Классический Factorio
PollutionColorGradient green = PollutionColorGradient.TOXIC_GREEN; // Радиация / токсины
PollutionColorGradient gray = PollutionColorGradient.SMOG_GRAY;   // Угольный смог / дым

// 2. Создание кастомного градиента (например, фиолетовая порча/магия):
PollutionColorGradient magicWarp = new PollutionColorGradient(List.of(
    new PollutionColorGradient.ColorStop(0.00F, 0x008800FF), // 0% плотности: прозрачный
    new PollutionColorGradient.ColorStop(0.20F, 0x309922FF), // 20%: легкая дымка
    new PollutionColorGradient.ColorStop(0.60F, 0x706600CC), // 60%: фиолетовый туман
    new PollutionColorGradient.ColorStop(1.00F, 0xB0440088)  // 100%: густая темная порча
));
```

---

## 5. Прямой доступ к клиентскому хранилищу (`ClientPollutionData`)

Если требуется программно прочитать или изменить значение на клиенте:

```java
import com.slavav.xaeroszones.client.pollution.ClientPollutionData;

// Записать значение:
ClientPollutionData.setPollution("minecraft:overworld", chunkX, chunkZ, 500.0F);

// Прочитать значение:
float current = ClientPollutionData.getPollution("minecraft:overworld", chunkX, chunkZ);

// Управление отображением:
ClientPollutionData.showPollution = true;          // Мастер-переключатель
ClientPollutionData.showPollutionWorldMap = true;  // Карта мира
ClientPollutionData.showPollutionMinimap = true;   // Миникарта
ClientPollutionData.pollutionOpacity = 0.85F;      // Прозрачность (0.1 - 1.0)
```

---

## 6. Пользовательские настройки в игре

Игроки могут настраивать отображение прямо в меню XaerosZones:
* **Клавиша `Z` $\rightarrow$ Настройки:**
  * **Отображение смога:** Глобальное включение/выключение.
  * **Смог на карте мира:** Отображение на большой карте (`M`).
  * **Смог на миникарте:** Отображение в угловом радаре.
  * **Сглаженное облако:** Переключение между почанковой сеткой Factorio и сглаженным органическим облаком.
  * **Прозрачность смога:** Слайдер от 0% до 100%.
