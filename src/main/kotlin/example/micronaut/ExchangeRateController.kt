package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.hateos.JsonError
import io.reactivex.Flowable


@Controller
class ExchangeRateController(private val exchangeRateService: ExchangeRateService) {

    @Post("/api/convert")
    fun getRate(@Body exchangeRateRequest: ExchangeRateRequest): Flowable<MutableHttpResponse<ExchangeRateResponse>> {
        return exchangeRateService.getRate(exchangeRateRequest)
                .map { HttpResponse.ok(it) } // global error handler does not work otherwise
    }

    @Error(global = true)
    fun error(request: HttpRequest<Any>, e: Throwable): HttpResponse<JsonError> =
            when (e) {
                is HttpClientResponseException -> HttpResponse.badRequest()
                else -> HttpResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
            }

}
