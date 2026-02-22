package miao.kmirror.miaolibrary.demo.data.repository

import miao.kmirror.miaolibrary.demo.data.local.StudentEntity
import miao.kmirror.miaolibrary.demo.data.remote.dto.StudentDto
import miao.kmirror.miaolibrary.demo.domain.model.Student

fun StudentEntity.toDomain() = Student(
    id = id,
    name = name,
    gender = gender,
    birthday = birthday,
    height = height,
    isLocked = isLocked,
    updateTime = updateTime
)

fun Student.toEntity() = StudentEntity(
    id = id,
    name = name,
    gender = gender,
    birthday = birthday,
    height = height,
    isLocked = isLocked,
    updateTime = updateTime
)

fun StudentDto.toEntity() = StudentEntity(
    id = id,
    name = name,
    gender = gender,
    birthday = birthday,
    height = height,
    isLocked = isLocked,
    updateTime = System.currentTimeMillis()
)