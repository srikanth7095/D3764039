package com.project.studyhub.quotes

import retrofit2.http.GET
import retrofit2.http.Headers

interface QuoteApiService {
    @Headers("x-api-key: JXI2tsIGtbOLZQl/eO7kew==inoH6hvRdtHrlvr4")
    @GET("v1/quotes?category=education")
    suspend fun getQuote(): List<QuoteResponse>
}
