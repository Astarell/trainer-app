swagger-ui http://localhost:8080/api/q/swagger-ui/

команды для генерации закрытого и открытого ключа
```sh
openssl genrsa -out privateKey.pem 2048
openssl rsa -in privateKey.pem -pubout -out publicKey.pem
```