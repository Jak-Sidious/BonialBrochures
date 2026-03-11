package com.jakana.bonialbrochures.data.remote

import com.jakana.bonialbrochures.data.model.ShelfResponse
import retrofit2.http.GET

interface ShelfApiService {
    @GET("shelf.json")

    suspend fun getShelf(): ShelfResponse
}