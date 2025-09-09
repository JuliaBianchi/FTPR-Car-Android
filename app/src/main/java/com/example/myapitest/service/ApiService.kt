package com.example.myapitest.service

import com.example.myapitest.model.Car
import com.example.myapitest.model.CarDetail
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/car") suspend fun getCars(): List<Car>
    @GET("/car/{id}") suspend fun getCarById(@Path("id") id: String): CarDetail
    @DELETE("/car/{id}") suspend fun deleteById(@Path("id") id: String)

}