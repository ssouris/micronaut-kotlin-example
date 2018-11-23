package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpResponse.badRequest
import io.micronaut.http.HttpResponse.status
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.reactivex.Flowable

@Controller
class ExchangeRateController(
        private val exchangeRateService: ExchangeRateService) {

    @Post("/api/convert")
    fun getRate(@Body exchangeRateRequest: ExchangeRateRequest): Flowable<MutableHttpResponse<ExchangeRateResponse>> {
        return exchangeRateService.getRate(exchangeRateRequest)

                // custom handling of errors since global error handler does not work
                // as expected with the RxJava client
                .map { HttpResponse.ok(it) }
                .onErrorReturn {
                    when (it) {
                        is HttpClientResponseException -> badRequest()
                        else -> status(HttpStatus.SERVICE_UNAVAILABLE)
                    }

                }
    }

}
