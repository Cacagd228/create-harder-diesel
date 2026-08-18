# Интеграция Create: Propulsion

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

## Зависимость и статус

- Файлы работают при наличии **Create: Propulsion**. При отсутствии мода
  JSON-файлы просто игнорируются (не вызывают ошибок).
- **Статус:** Create: Propulsion пока не имеет сборки для MC 1.21.1. Интеграция
  подготовлена и заработает при портировании Propulsion на 1.21.1.
