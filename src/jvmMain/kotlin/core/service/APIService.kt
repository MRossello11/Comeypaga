package core.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager

fun createRetrofit(baseUrl: String): Retrofit {
    val trustAllCerts = arrayOf<TrustManager>(SelfSignedTrustManager())
    val sslContext = SSLContext.getInstance("SSL").apply {
        init(null, trustAllCerts, SecureRandom())
    }

    val cookieJar = ComeCookies()

    val client = OkHttpClient.Builder()
        .sslSocketFactory(sslContext.socketFactory, SelfSignedTrustManager())
        .hostnameVerifier { _, _ -> true }
        .cookieJar(cookieJar)
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client) // Set the OkHttpClient with the cookie jar
        .build()
}