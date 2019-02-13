package ua.piraeusbank.banking.common.config

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import okhttp3.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RestAuthConfig {

    @Bean
    fun tokenCache(): LoadingCache<String, String> {
        val objectMapper = ObjectMapper()

        val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("scope", "server")
                .addFormDataPart("client_id", "ms-account")
                .addFormDataPart("grant_type", "client_credentials")
                .build()

        val loader: CacheLoader<String, String>
        loader = object : CacheLoader<String, String>() {
            override fun load(key: String): String {
                val body = OkHttpClient().newBuilder().build().newCall(
                        Request.Builder()
                                .url("http://192.168.0.102:8001/auth/oauth/token")
                                .post(requestBody)
                                .addHeader(
                                        "Authorization",
                                        Credentials.basic("ms-account", ""))
                                .build()).execute().body()

                val accessTokenResponse = objectMapper.readValue(body.string(), AccessTokenResponse::class.java)
                return accessTokenResponse.accessToken
            }
        }
        return CacheBuilder.newBuilder().build<String, String>(loader)
    }

    @Bean("tokenAuthenticator")
    fun tokenAuthenticator(tokenCache: LoadingCache<String, String>): Interceptor {
        return TokenAuthenticator(tokenCache)
    }
}

class TokenAuthenticator(@Autowired private val tokenCache: LoadingCache<String, String>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
                .header("Authorization", "Bearer ${tokenCache["token"]}")
                .build()
        return chain.proceed(request)
    }
}

data class AccessTokenResponse(
        @JsonProperty("access_token") val accessToken: String,
        @JsonProperty("token_type") val tokenType: String,
        @JsonProperty("expires_in") val expiresIn: String,
        @JsonProperty("scope") val scope: String
)
