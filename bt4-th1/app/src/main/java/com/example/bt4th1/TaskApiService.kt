package com.example.bt4th1

import retrofit2.Response
import retrofit2.http.GET

interface TaskApiService {
    @GET("tasks")
    suspend fun getTasks(): Response<ApiResponse>
}