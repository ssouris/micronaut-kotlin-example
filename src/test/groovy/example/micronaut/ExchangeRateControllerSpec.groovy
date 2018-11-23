package example.micronaut


import example.micronaut.ExchangeRateRequest
import example.micronaut.ExchangeRateResponse
import example.micronaut.mocks.MockExchangeRateApiClient
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ExchangeRateControllerSpec extends Specification {

    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    @Shared
    @AutoCleanup
    RxHttpClient client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())

    @Unroll
    def "test exchange rate controller response with (#fromCurrency, #toCurrency, #amount)"() {

        def rateReq = new ExchangeRateRequest(fromCurrency, toCurrency, amount)
        when:
        HttpRequest request = HttpRequest.POST('/api/convert', rateReq)
        ExchangeRateResponse rsp = client.toBlocking().retrieve(request, ExchangeRateResponse.class)

        then:
        rsp.original == rateReq.amount
        rsp.exchange == MockExchangeRateApiClient.rates[rateReq.toCurrency]
        rsp.amount == MockExchangeRateApiClient.rates[rateReq.toCurrency] * rateReq.amount

        where:
        fromCurrency | toCurrency | amount
        'EUR'        | 'USD'      | 100
        'EUR'        | 'EUR'      | 100
        'USD'        | 'EUR'      | 100
    }

}
