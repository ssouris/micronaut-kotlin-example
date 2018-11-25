package example.micronaut

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.reactivex.Flowable

@Client("exchangeratesapi")
interface ExchangeRateApiClient : ExchangeRateApi

interface ExchangeRateApi {

    @Get("/latest?base={fromCurrency}")
    fun findRates(fromCurrency: String): Flowable<ExternalApiResponse>

}
