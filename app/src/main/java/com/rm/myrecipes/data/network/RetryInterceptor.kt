package com.rm.myrecipes.data.network


import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetryInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return process(chain, attempt = 1)
    }

    private fun process(chain: Interceptor.Chain, attempt: Int): Response {
        var response: Response? = null
        return try {
            val request = chain.request()
            response = chain.proceed(request)
            if (attempt < RETRY && !response.isSuccessful) {
                return delayedRetry(chain, response, attempt)
            }
            response
        } catch (e: Exception) {
            Timber.d("Recipe: $e")
            throw e
        }
    }

    private fun delayedRetry(
        chain: Interceptor.Chain,
        response: Response,
        attempt: Int,
    ): Response {
        Timber.d("Recipe: RETRY fetch attempt: $attempt")
        response.body?.close()
        Thread.sleep(INTERVAL * attempt)
        return process(chain, attempt = attempt + 1)
    }

    companion object {
        const val RETRY = 3+1
        const val INTERVAL = 1000L
    }
}
