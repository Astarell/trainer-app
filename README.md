# Кейс 1. Тренажёр аналитики

Приложение-тренажёр, который позволит аналитикам отрабатывать практические навыки в безопасной и контролируемой среде.

Приложение имитирует реальную работу аналитика: пользователи проходят задания разных типов, система фиксирует результаты, начисляет баллы и ведёт прогресс обучения.

В системе реализовано три вида заданий:
* тестовые задания (один или несколько правильных ответов);
* задания на поиск ошибок;
* открытые задания (самостоятельная проработка артефактов).

## Состав команды

* Цылев Артем - тимлид, настройка БД, заполнение тестовыми данными, описание сущностей БД, docker-compose
* Вартик Дмитрий - аутентификация, api trainers, валидация
* Монич Никита - frontend
* Кулачкова Анастасия - api trainers, profile, expert
* Трошина Анастасия - контейнеризация
* Малнар Иван

[Распределение](https://docs.google.com/spreadsheets/d/1EO8NqOaiaRsioVb-cQi24zAXVEvAujtDzxpHvW2JcFU/edit?gid=0#gid=0)

## Инструкция по запуску
Убедиться, что докер запущен локально и у него есть возможность скачивать образы.
```sh
docker-compose -f compose-devservices.yml up --build
```

## Swagger
swagger-ui http://localhost:8080/api/q/swagger-ui/

## Тестирование
### Тестовые данные
Команда подготовила небольшое количество трейнеров с заданиями, типы которых указаны в требованиях.<br>
Идентификаторы трейнеров (можно получить содержимое трейнера по спец. ручке через сваггер):
1. SQL trainer #1: bcc6f453-e4e4-49fc-b1e3-ca687d3f20a6
2. Test trainer #2: 34f097a9-1542-4a43-8b30-bef905d4f925
3. Business analyses trainer #3: 8d3d4a57-c179-4f34-afc5-6a4e272fc26b

### Ход тестирования
👇
<details>

#### 1.1. Регистрация пользователя

```
curl -X 'POST' \
  'http://localhost:8080/api/auth/register' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "Иван",
  "lastName": "Иванов",
  "userRole": "APP_USER"
}'
```

<img width="896" height="405" alt="image" src="https://github.com/user-attachments/assets/341fdd6d-d7cc-48e4-911f-9d5c930c93db" />

#### 1.2. Регистрация эксперта
```
curl -X 'POST' \
  'http://localhost:8080/api/auth/register' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "email": "expert@example.com",
  "password": "password123",
  "firstName": "Иван",
  "lastName": "Иванов",
  "userRole": "APP_EXPERT"
}'
```
**Формат ответа:**
<img width="866" height="251" alt="image" src="https://github.com/user-attachments/assets/15dc0159-55c2-474e-b5ef-c28f574d5652" />


#### 2. Вход в систему

```
curl -X 'POST' \
  'http://localhost:8080/api/auth/login' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "email": "user@example.com",
  "password": "password123"
}'
```
**Формат ответа:**
<img width="885" height="271" alt="image" src="https://github.com/user-attachments/assets/761e4311-2db1-4b74-844f-d1ed350762c2" />


#### 3. Получить список имеющихся тренажеров
```
curl -X 'GET' \
  'http://localhost:8080/api/trainers' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer TOKEN'
```

**Формат ответа:**

<img width="887" height="384" alt="image" src="https://github.com/user-attachments/assets/d65c73ed-2471-4ed7-a566-09a8d102742d" />


#### 4. Получить конкретный тренажер по ID
```
curl -X 'GET' \
  'http://localhost:8080/api/trainers/bcc6f453-e4e4-49fc-b1e3-ca687d3f20a6' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer TOKEN'
```

**Формат ответа:**

<img width="883" height="545" alt="image" src="https://github.com/user-attachments/assets/6714addc-1c17-4157-b36d-9997ae841636" />
<img width="688" height="430" alt="image" src="https://github.com/user-attachments/assets/7cc6ba5a-6bb7-4d0f-be66-3009dbd6339a" />



#### 5. Получить выбранную задачу из списка задач тренажера
```
curl -X 'GET' \
  'http://localhost:8080/api/trainers/bcc6f453-e4e4-49fc-b1e3-ca687d3f20a6/tasks/9bf433c6-301b-4458-8be7-aea348f64b44' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer TOKEN'
```

**Формат ответа:**

<img width="874" height="360" alt="image" src="https://github.com/user-attachments/assets/5682ff1f-f16a-4f3e-945c-8f6dbb31f664" />


#### 6.1. Отправить ответ на выбранный вопрос из списка вопросов тренажера (тестовое задание)
```
curl -X 'POST' \
  'http://localhost:8080/api/trainers/bcc6f453-e4e4-49fc-b1e3-ca687d3f20a6/tasks/8140dc03-762c-45c9-9bf7-e822f514ca43/submit' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer TOKEN' \
  -H 'Content-Type: application/json' \
  -d '{
  "userAnswer": 2
}'
```
**Формат ответа:**

<img width="895" height="186" alt="image" src="https://github.com/user-attachments/assets/37882a97-b002-415a-af2f-07a496e7ddf9" />

<img width="863" height="178" alt="image" src="https://github.com/user-attachments/assets/48f82ef4-e71e-43d2-8b8e-24235b9c2cea" />


#### 6.2. Отправить ответ на выбранный вопрос из списка вопросов тренажера (открытый вопрос)
```
curl -X 'POST' \
  'http://localhost:8080/api/trainers/bcc6f453-e4e4-49fc-b1e3-ca687d3f20a6/tasks/9bf433c6-301b-4458-8be7-aea348f64b44/submit' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer TOKEN' \
  -H 'Content-Type: application/json' \
  -d '{
  "userAnswer": "Ответ на задание"
}'
```
**Формат ответа:**
<img width="891" height="192" alt="image" src="https://github.com/user-attachments/assets/c172ca62-95fd-4521-a1d4-488ab90060f0" />
<img width="893" height="208" alt="image" src="https://github.com/user-attachments/assets/c59092b3-4610-4700-a693-3602194c7c9c" />

#### 6.3. Отправить ответ на выбранный вопрос из списка вопросов тренажера (поиск ошибок)
```
curl -X 'POST' \
  'http://localhost:8080/api/trainers/bcc6f453-e4e4-49fc-b1e3-ca687d3f20a6/tasks/fa74c630-97b4-40ed-a74f-8ee885573b33/submit' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer TOKEN' \
  -H 'Content-Type: application/json' \
  -d '{
  "userAnswer": "Ответ на задание"
}'
```
**Формат ответа:**

<img width="889" height="181" alt="image" src="https://github.com/user-attachments/assets/c2f1a4f0-51f2-4231-94ca-b3fff69adc71" />
```
curl -X 'POST' \
  'http://localhost:8080/api/trainers/bcc6f453-e4e4-49fc-b1e3-ca687d3f20a6/tasks/fa74c630-97b4-40ed-a74f-8ee885573b33/submit' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer TOKEN' \
  -H 'Content-Type: application/json' \
  -d '{
  "userAnswer": "FROM"
}'
```
**Формат ответа:**

<img width="876" height="206" alt="image" src="https://github.com/user-attachments/assets/cc5cff27-da75-404c-a65f-3d32a76d98dd" />

#### 6.4. Отправить ответ на выбранный вопрос из списка вопросов тренажера (выбор нескольких вариантов)
```
curl -X 'POST' \
  'http://localhost:8080/api/trainers/bcc6f453-e4e4-49fc-b1e3-ca687d3f20a6/tasks/cdb2e661-0875-4051-8770-6aeffaa40b1e/submit' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer TOKEN' \
  -H 'Content-Type: application/json' \
  -d '{
  "userAnswer": "[1]"
}'
```
**Формат ответа:**

<img width="884" height="191" alt="image" src="https://github.com/user-attachments/assets/8c14e025-fcd8-41b2-8da1-25070de0a844" />

```
curl -X 'POST' \
  'http://localhost:8080/api/trainers/bcc6f453-e4e4-49fc-b1e3-ca687d3f20a6/tasks/cdb2e661-0875-4051-8770-6aeffaa40b1e/submit' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer TOKEN' \
  -H 'Content-Type: application/json' \
  -d '{
  "userAnswer": "[4,2]"
}'
```
**Формат ответа:**
<img width="875" height="164" alt="image" src="https://github.com/user-attachments/assets/855e756c-4db5-408e-8830-0345e2860167" />


#### 7. Получить профиль
```
curl -X 'GET' \
  'http://localhost:8080/api/profile' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer TOKEN'
```
**Формат ответа:**
<img width="874" height="475" alt="image" src="https://github.com/user-attachments/assets/08d04a58-2c91-48c9-9cc9-fb7f96f9477d" />

После проверки экспертом:
<img width="870" height="488" alt="image" src="https://github.com/user-attachments/assets/974ef31c-f4ed-4e34-a092-c2c75481b851" />


#### 8. Получить прогресс пользователя по тренажеру
```
curl -X 'GET' \
  'http://localhost:8080/api/profile/trainers/bcc6f453-e4e4-49fc-b1e3-ca687d3f20a6' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer TOKEN'
```
**Формат ответа:**
<img width="880" height="548" alt="image" src="https://github.com/user-attachments/assets/6a0e0744-0378-439a-be14-1d461c9951b9" />
После проверки экспертом:

<img width="877" height="545" alt="image" src="https://github.com/user-attachments/assets/dead24e8-dfb0-432b-9da2-7d01a4cb07ec" />
<img width="908" height="549" alt="image" src="https://github.com/user-attachments/assets/ed0b8a38-19de-4f78-bc27-9a19a79473f8" />


#### 9. Получить список задач на проверку
```
curl -X 'GET' \
  'http://localhost:8080/api/expert/examination' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer TOKEN'
```
**Формат ответа:**
<img width="884" height="277" alt="image" src="https://github.com/user-attachments/assets/6a3d554b-5bbe-409c-9b57-a70daf46b073" />


#### 10. Посмотреть ответ пользователя
```
curl -X 'GET' \
  'http://localhost:8080/api/expert/examination/b9d47cd3-0a03-42f3-8531-6566bc5f3f41' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer TOKEN'
```
**Формат ответа:**
<img width="865" height="314" alt="image" src="https://github.com/user-attachments/assets/932ff20c-28c9-4af0-b2f7-afa54250d441" />


#### 11. Выставить балл
```
curl -X 'POST' \
  'http://localhost:8080/api/expert/examination/b9d47cd3-0a03-42f3-8531-6566bc5f3f41' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer TOKEN' \
  -H 'Content-Type: application/json' \
  -d '{
  "isCorrect": "true"
}'
```
**Формат ответа:**
<img width="884" height="187" alt="image" src="https://github.com/user-attachments/assets/32ccf3d2-5746-4e1a-8f92-1f6bc52e4008" />

<img width="863" height="197" alt="image" src="https://github.com/user-attachments/assets/7fe5b282-2887-4128-8875-8385d2ca78b2" />


</details>

## Схема БД
### Диаграмма
👇
<details>
  <img width="993" height="859" alt="postgres - public" src="https://github.com/user-attachments/assets/8ca8b101-bca7-4049-9e96-ae4ba55a07d5" />
</details>

### Основные сущности
- **`users`** - учетные записи.<br>
  Хранит `id` (UUID), ФИО, уникальный `email`, хеш пароля и системную роль (`app_role`).
- **`trainers`** - логические контейнеры для заданий.<br>
  Поля: `id`, `name`, `created_by` (ссылка на `users`), `created_at`. 
- **`tasks`** - конкретное упражнение.<br>
  Содержит `id`, тип (`task_type`), автора и `config` (JSONB). Конфиг может содержать произвольное кол-во полей, напр.: вопрос, эталонный ответ, макс. баллы, штраф за ошибку и лимит попыток.
- **`tasks_trainers`** - задача в конкретном тренажере.<br>
- **`task_attempts`** - история прохождений.<br>
  Фиксирует результат попытки пользователя: `user_answer` (JSONB), набранные `points`, `status` и временную метку. Привязывается к конкретной связке задание-тренажер.

### Связи
- `trainers` ↔ `tasks`: связь Many-to-Many, реализуется через таблицу `tasks_trainers` (`task_id`, `trainer_id` + уникальность пары). При удалении сущностей срабатывает каскадное удаление из смежной таблицы.
- `users` → `task_attempts`: One-to-Many. Один пользователь может иметь множество попыток.
- `tasks_trainers` → `task_attempts`: One-to-Many. Попытка привязывается к конкретному вхождению задания в тренажер.

### Перечисления
- `user_roles`: APP_USER, APP_EXPERT, APP_ADMIN
- `task_types`: SINGLE_CHOICE, MULTIPLE_CHOICE, ERROR_FINDING, OPEN_ANSWER
- `task_attempts_status`: REVIEW, COMPLETED, FAILED


### Формат конфигурации задач

Ниже представлены примеры конфигурации задач

#### SINGLE_CHOICE (Одиночный выбор)
```json
{
  "question": "Какой вариант правильный?",
  "answerChoices" : [
    {    
      "ordinal" : 1,
      "choice" : "Вариант 1"
    },
    {    
      "ordinal" : 2,
      "choice" : "Вариант 2"
    }
  ],
  "expectedOrdinal": 1,
  "points": 2,
  "mistakeCost": 2,
  "maxAttempts": 1
}
```

#### MULTIPLE_CHOICE (Несколько правильных ответов)

```json
{
  "question": "Какой вариант правильный?",
  "answerChoices" : [
    {    
      "ordinal" : 1,
      "choice" : "Вариант 1"
    },
    {    
      "ordinal" : 2,
      "choice" : "Вариант 2"
    }
  ],
  "expectedOrdinal": [1, 2],
  "points": 4,
  "mistakeCost": 2,
  "maxAttempts": 2
}
```

#### ERROR_FINDING (Поиск ошибок в выражении)

```json
{
  "question": "Найдите ошибку в выражении в ответ запишите одно слово, которое должно быть заменено",
  "answer": "Эталонный ответ",
  "points": 5,
  "mistakeCost": 2,
  "maxAttempts": 2
}
```

#### OPEN_ANSWER (Открытые задания)

```json
{
  "question": "Вопрос с открытым ответом",
  "context": "Описание задания",
  "answer": "",
  "points": 8,
  "mistakeCost": 3,
  "maxAttempts": 2
}
```

#### Примечания
* Для OPEN_ANSWER ответ отправляется на проверку эксперту (статус REVIEW), до его проверки points=0
* Остальные типы задач проверяются автоматически
* При повторных попытках применяется штраф, если был дан правильный ответ: points = maxPoints - mistakeCost * (attemptsCount - 1)
* При неверном ответе выставляется points=0


## Структура проекта

```
src/main
├── java
│   └── ru
│       └── mephi
│           └── trainer
│               ├── config                 # Конфигурация OpenAPI и Swagger
│               ├── entity                 # Сущности
│               │   └── enums              # Перечисления 
│               ├── exception              # Кастомные исключения 
│               ├── mapper                 # Мапперы
│               ├── models                 # Модели
│               ├── repository             # Panache репозитории для работы с БД
│               ├── rest
│               │   ├── api                # OpenAPI интерфейсы
│               │   ├── controller         # REST контроллеры
│               │   ├── dto                # Data Transfer Objects
│               │   │   ├── request        # DTO для запросов
│               │   │   └── response       # DTO для ответов
│               │   └── handler            # Обработчик
│               └── service                # Бизнес-логика
└── resources
    └── db
        └── changelog                      # Миграции Liquibase
            └── versions                   # SQL скрипты миграций
```


## Пример использования сервиса

### Запрос для проверки заданий
SINGLE_CHOICE
```
curl -X 'POST' \
  'http://localhost:8080/api/trainers/{trainer-id}/tasks/{task-id}/submit' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer {token}' \
  -d '{
  "userAnswer": "1"
}'
```
MULTIPLE_CHOICE
```
curl -X 'POST' \
  'http://localhost:8080/api/trainers/{trainer-id}/tasks/{task-id}/submit' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer {token}' \
  -d '{
  "userAnswer": "[1, 2]"
}'
```
ERROR_FINDING и OPEN_ANSWER
```
curl -X 'POST' \
  'http://localhost:8080/api/trainers/{trainer-id}/tasks/{task-id}/submit' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer {token}' \
  -d '{
  "userAnswer": "Текст ответа"
}'
```

Примечания
* {trainer-id} — UUID тренажёра
* {task-id} — UUID задачи
* {token} — JWT токен, полученный при входе
