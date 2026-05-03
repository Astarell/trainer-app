swagger-ui http://localhost:8080/api/q/swagger-ui/

команды для генерации закрытого и открытого ключа
```sh
openssl genrsa -out privateKey.pem 2048
openssl rsa -in privateKey.pem -pubout -out publicKey.pem
```

## Поднимаем прокси контейнер (необходим для сборки):

```sh
docker run -d --name squid-container \
-p 3128:3128 \
ubuntu/squid:7.2-26.04_edge
```

Можно посмотреть логи его работы:
```sh
docker logs -f squid-container
```

## Определяем адрес маршрутизации изнутри контейнера:
```sh
docker run --rm alpine sh -c "ip route | awk '/default/ { print $3 }'"
```

## Запускаем сборку (вносим адрес, полученный из предыдущей команды):
```sh
docker buildx build . --add-host="host.docker.internal:172.17.0.1" -t trainer-app:1.0
```

## Запускаем приложение
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
