package example.micronaut


/**
 * API request object
 */
data class ExchangeRateRequest(val fromCurrency: String, val toCurrency: String, val amount: Double)

/**
 * API response object
 */
data class ExchangeRateResponse(val exchange: Double, val amount: Double, val original: Double)

/**
 * External API valid response object
 */
data class ExternalApiResponse(val date: String, val rates: Map<String, Double>, val base: String)
