### Launch
```
./gradlew clean bootJar && docker compose up  
```


### Get board view
```
curl --location --request GET 'localhost:8080/orders/boardView' \
--header 'Accept: application/json'
```

### Create buy order
```
curl --location --request POST 'localhost:8080/orders/buy' \
--header 'X-AUTH-USER-ID: 1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "pricePerUnit": {
        "amount": 13.123,
        "currencyCode": "USD"
    },
    "quantity": {
        "amount": 13,
        "unit": "KG"
    }
}'
```

### Create sell order
```
curl --location --request POST 'localhost:8080/orders/sell' \
--header 'X-AUTH-USER-ID: 1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "pricePerUnit": {
        "amount": 13.22,
        "currencyCode": "USD"
    },
    "quantity": {
        "amount": 15,
        "unit": "KG"
    }
}'
```

### delete order by id
```
curl --location --request DELETE 'localhost:8080/orders/@ORDER_ID@' \
--header 'X-AUTH-USER-ID: 1' \
--header 'Content-Type: application/json'
