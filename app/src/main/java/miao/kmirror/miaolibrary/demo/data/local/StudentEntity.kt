package miao.kmirror.miaolibrary.demo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey val id: String,
    val name: String,
    val gender: String,
    val birthday: Long,
    val height: Int,
    val isLocked: Boolean,
    val updateTime: Long
)