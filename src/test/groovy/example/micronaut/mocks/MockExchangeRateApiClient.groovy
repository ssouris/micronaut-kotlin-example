package example.micronaut.mocks

import javax.inject.Singleton

import example.micronaut.ExchangeRateApi
import example.micronaut.ExchangeRateApiClient
import example.micronaut.ExternalApiResponse
import io.micronaut.context.annotation.Replaces
import io.reactivex.Flowable

@Singleton
@Replaces(value = ExchangeRateApiClient.class)
class MockExchangeRateApiClient implements ExchangeRateApi {

    public static final def rates = [EUR: 1.1, USD: 1.2] as Map<String, Double>

    @Override
    Flowable<ExternalApiResponse> findRates(String fromCurrency) {
        Flowable.fromArray(new ExternalApiResponse(new Date().toString(), rates, fromCurrency))
    }

}
