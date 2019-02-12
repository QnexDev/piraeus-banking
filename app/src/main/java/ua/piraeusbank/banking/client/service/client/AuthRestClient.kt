package ua.piraeusbank.banking.client.service.client

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Credentials
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import ua.piraeusbank.banking.client.config.AccessTokenResponse

object AuthRestClient {

    fun obtainAccessToken(username: String, password: String): String {
        val objectMapper = ObjectMapper()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("scope", "ui")
            .addFormDataPart("username", username)
            .addFormDataPart("password", password)
            .addFormDataPart("grant_type", "password")
            .build()

        val response = OkHttpClient().newBuilder().build().newCall(
            Request.Builder()
                .url("http://192.168.0.102:8001/auth/oauth/token")
                .post(requestBody)
                .addHeader(
                    "Authorization",
                    Credentials.basic("browser", "")
                )
                .build()
        ).execute()
        val body = response.body()

        if (response.code() in 400..500) {
            throw RuntimeException("Wrong credentials!")
        }

        if (!response.isSuccessful) {
            throw RuntimeException("Couldn't obtain access token")
        }

        val accessTokenResponse = objectMapper.readValue(body.string(), AccessTokenResponse::class.java)

        return accessTokenResponse.accessToken
    }
}