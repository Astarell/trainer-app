# Кейс 1. Тренажёр аналитики

## Инструкция по запуску
### Поднимаем прокси контейнер (необходим для сборки):

```sh
docker run -d --name squid-container \
-p 3128:3128 \
ubuntu/squid:7.2-26.04_edge
```

Можно посмотреть логи его работы:
```sh
docker logs -f squid-container
```

### Определяем адрес маршрутизации изнутри контейнера:
```sh
docker run --rm alpine sh -c "ip route | awk '/default/ { print $3 }'"
```

### Запускаем сборку (вносим адрес, полученный из предыдущей команды):
```sh
docker buildx build . --add-host="host.docker.internal:172.17.0.1" -t trainer-app:1.0
```

### Запускаем приложение
Создадим сегмент сети для работы базы данных и нашего приложения:
```sh
docker network create quarkus-network
```

Запускаем базу данных (необходимую для приложения):
```sh
docker run -d --name trainer-db --network quarkus-network \
-e POSTGRES_USER=quarkus \
-e POSTGRES_PASSWORD=quarkus \
-e POSTGRES_DB=postgres-db \
postgres:18-alpine
```

Запускаем приложение из образа в интерактивном режиме:

```sh
docker run -i --name trainer-app --rm -p 8080:8080 --network quarkus-network \
-e QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://trainer-db:5432/postgres \
-e QUARKUS_DATASOURCE_USERNAME=quarkus \
-e QUARKUS_DATASOURCE_PASSWORD=quarkus \
trainer-app:1.0
```

Подключение в контейнер trainer-app при необходимости:

```sh
docker exec -it trainer-app sh
```

## Swagger
swagger-ui http://localhost:8080/api/q/swagger-ui/

команды для генерации закрытого и открытого ключа
```sh
openssl genrsa -out privateKey.pem 2048
openssl rsa -in privateKey.pem -pubout -out publicKey.pem
```

## Схема БД
### Диаграмма
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
- `task_types`: SINGLE_CHOICE, MULTIPLE_CHOICE, ERROR_FINDING, OPEN_ANSWER, SQL_QUERY
- `task_attempts_status`: REVIEW, COMPLETED, FAILED



