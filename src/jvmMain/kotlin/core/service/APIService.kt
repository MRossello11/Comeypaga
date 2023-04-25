package core.service

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

fun createRetrofit(baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
}