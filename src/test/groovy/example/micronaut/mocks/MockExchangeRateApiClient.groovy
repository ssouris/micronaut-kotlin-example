package example.micronaut.mocks

import javax.inject.Singleton

import example.micronaut.ExchangeRateApi
import example.micronaut.ExchangeRateApiClient
import example.micronaut.ExternalApiResponse
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.reactivex.Flowable

@Singleton
@Replaces(value = ExchangeRateApiClient.class)
class MockExchangeRateApiClient implements ExchangeRateApi {

    public static final def rates = [EUR: 1.1, USD: 1.2] as Map<String, Double>
    public static final String BAD__REQUEST = "BAD_REQUEST"
    public static final String SERVICE_UNAVAILABLE = "SERVICE_UNAVAILABLE"

    @Override
    Flowable<ExternalApiResponse> findRates(String fromCurrency) {
        if (fromCurrency == BAD__REQUEST) {
            return Flowable.error(new HttpClientResponseException("", HttpResponse.ok()))
        } else if (fromCurrency == SERVICE_UNAVAILABLE) {
            return Flowable.error(new RuntimeException())
        }
        return Flowable.fromArray(new ExternalApiResponse(new Date().toString(), rates, fromCurrency))
    }

}
