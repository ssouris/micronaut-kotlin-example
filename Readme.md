# Micronaut example

Simple micro-service that queries and caches an exchange rate API to convert an amount to a currency

example request:
```
curl -v -X POST localhost:8080/api/convert \
    -d '{"fromCurrency" : "EUR", "toCurrency" : "USD", "amount": 1600 }' 
    -H 'Content-Type: application/json' | jq
```

Tech used:
- Kotlin
- Micronaut with Non-blocking API(s)
- Caffeine cache through Micronaut
- Spock for testing

Caveats:
- Global error handling did not work really well with reactive types (no way to catch exception inside reactive stream and return custom status code)

Useful links:
- https://medium.com/mindorks/rxjava2-and-retrofit2-error-handling-on-a-single-place-8daf720d42d6
- https://www.slideshare.net/alvarosanchezmariscal/reactive-microservices-with-micronaut-gr8conf-eu-2018
- http://mrhaki.blogspot.com/2018/08/micronaut-mastery-return-response-based.html
- https://alvarosanchez.github.io/micronaut-workshop
- https://github.com/alvarosanchez/micronaut-workshop-java
- https://github.com/mrhaki/spring-micronaut-sample/
- https://medium.com/@mesirii/ad-astra-the-micronaut-framework-52ff2d684877
