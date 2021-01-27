package com.honeywell.assignment.api


import com.honeywell.assignment.model.SearchResponse
import com.o2finfosolutions.task.api.APIEndPoint
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HoneyWellServices {
    @GET(APIEndPoint.DYNAMIC_LIST)
    fun getArticles(@Query("query") query:String,@Query("page")page:String): Call<SearchResponse>
}