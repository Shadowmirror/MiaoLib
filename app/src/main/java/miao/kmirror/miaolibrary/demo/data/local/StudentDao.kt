package miao.kmirror.miaolibrary.demo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Query("SELECT * FROM students")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(students: List<StudentEntity>)

    @Query("UPDATE students SET isLocked = :isLocked WHERE id = :id")
    suspend fun updateLockStatus(id: String, isLocked: Boolean)

    @Query("SELECT COUNT(*) FROM students")
    suspend fun getCount(): Int
}