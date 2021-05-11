package com.example.comicstore

import retrofit2.Response
import retrofit2.http.GET

interface APIService {
    @GET("comics?apikey=9c40ff86cfd00882d81233dc85a10052&ts=0&hash=3460e37ce0f42aaab22ea73f3f361740")
    suspend fun getComics() : Response<Results>
}