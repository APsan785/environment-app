package com.apsan.environmentapp.api

import com.apsan.environmentapp.data.Quotes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface QuotesAPI {

    companion object{
        const val BASE_URL = "https://famous-quotes4.p.rapidapi.com/"
        const val API_KEY = "81700bca5cmsh83fea1ee4763196p170479jsnce21ad5e5d6d"
        const val API_HOST = "famous-quotes4.p.rapidapi.com"
    }

    @GET("random")
    @Headers(
        "x-rapidapi-host:$API_HOST",
        "x-rapidapi-key:$API_KEY"
    )

    suspend fun getEnvironmentQuotes(
        @Query("category") catg:String = "environmental",
        @Query("count") count:Int = 1
    ) : Response<List<Quotes>>
}