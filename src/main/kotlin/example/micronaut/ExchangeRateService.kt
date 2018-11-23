package example.micronaut

import io.micronaut.cache.annotation.CachePut
import io.micronaut.cache.annotation.Cacheable
import io.micronaut.cache.interceptor.CacheKeyGenerator
import io.micronaut.core.annotation.AnnotationMetadata
import io.reactivex.Flowable
import javax.inject.Singleton

@Singleton
@Cacheable(cacheNames = ["external-api-cache"])
class ExchangeRateService(val exchangeRateApi: ExchangeRateApi) {

    fun getRate(exchangeRateRequest: ExchangeRateRequest): Flowable<ExchangeRateResponse> {
        return getLatestExchangeRates(exchangeRateRequest)
                .flatMap {
                    val rate = it.rates.getOrDefault(exchangeRateRequest.toCurrency, 0.0)
                    Flowable.just(
                            ExchangeRateResponse(rate, rate * exchangeRateRequest.amount, exchangeRateRequest.amount))
                }
    }

    @CachePut(cacheNames = ["external-api-cache"], keyGenerator = ExternalApiCacheKey::class)
    fun getLatestExchangeRates(exchangeRateRequest: ExchangeRateRequest): Flowable<ExternalApiResponse> {
        return exchangeRateApiRequest(exchangeRateRequest)
                .flatMap { Flowable.just(it) }
    }

    private fun exchangeRateApiRequest(exchangeRateRequest: ExchangeRateRequest): Flowable<ExternalApiResponse> {
        return exchangeRateApi.findRates(exchangeRateRequest.fromCurrency)
    }

    class ExternalApiCacheKey : CacheKeyGenerator {
        override fun generateKey(annotationMetadata: AnnotationMetadata, vararg params: Any): Any? {
            val firstArg = params[0]
            if (firstArg is ExchangeRateRequest)
                return firstArg.fromCurrency
            return null
        }

    }


}
