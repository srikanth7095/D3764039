package com.project.studyhub.quotes

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object QuoteApiClient {
    private const val BASE_URL = "https://api.api-ninjas.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val quoteApiService: QuoteApiService by lazy {
        retrofit.create(QuoteApiService::class.java)
    }
}
