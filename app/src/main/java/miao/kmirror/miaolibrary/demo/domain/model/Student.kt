package miao.kmirror.miaolibrary.demo.domain.model

import java.util.Calendar

data class Student(
    val id: String,
    val name: String,
    val gender: String,
    val birthday: Long,
    val height: Int,
    val isLocked: Boolean = true,
    val updateTime: Long = System.currentTimeMillis()
) {
    val age: Int get() = 2026 - Calendar.getInstance().apply { timeInMillis = birthday }.get(Calendar.YEAR)
}
