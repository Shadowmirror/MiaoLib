package miao.kmirror.miaolibrary.demo.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudentDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("gender") val gender: String,
    @SerialName("birthday") val birthday: Long,
    @SerialName("height") val height: Int,
    @SerialName("isLocked") val isLocked: Boolean // 映射后端下划线命名
)