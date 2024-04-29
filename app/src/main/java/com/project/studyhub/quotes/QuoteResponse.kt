package com.project.studyhub.quotes

import com.google.gson.annotations.SerializedName

data class QuoteResponse(
    @SerializedName("quote") val quote: String,
    @SerializedName("author") val author: String,
    @SerializedName("category") val category: String
)
