package miao.kmirror.miaolibrary.demo.data.remote.api

import miao.kmirror.miaolibrary.demo.data.remote.dto.StudentDto
import retrofit2.Response
import retrofit2.http.GET

interface StudentApi {
    @GET("students") // 对应 127.0.0.1/students
    suspend fun getStudents(): Response<List<StudentDto>>
}