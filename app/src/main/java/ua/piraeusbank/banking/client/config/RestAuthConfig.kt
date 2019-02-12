package ua.piraeusbank.banking.client.config

import com.fasterxml.jackson.annotation.JsonProperty
import okhttp3.Interceptor
import okhttp3.Response
import ua.piraeusbank.banking.client.util.CurrentUserContext

object RestAuthConfig {

    fun tokenAuthenticator(): Interceptor {
        return TokenAuthenticator
    }
}

object TokenAuthenticator : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
                .header("Authorization", "Bearer ${CurrentUserContext.accessToken}")
                .build()
        return chain.proceed(request)
    }
}

data class AccessTokenResponse(
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("token_type") val tokenType: String,
    @JsonProperty("refresh_token") val refreshToken: String,
    @JsonProperty("expires_in") val expiresIn: String,
    @JsonProperty("scope") val scope: String
)