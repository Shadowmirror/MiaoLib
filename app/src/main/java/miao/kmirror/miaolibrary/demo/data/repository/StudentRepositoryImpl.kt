package miao.kmirror.miaolibrary.demo.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import miao.kmirror.miaolibrary.demo.data.local.StudentDao
import miao.kmirror.miaolibrary.demo.data.remote.api.StudentApi
import miao.kmirror.miaolibrary.demo.di.IoDispatcher
import miao.kmirror.miaolibrary.demo.domain.model.Student
import miao.kmirror.miaolibrary.demo.domain.repository.StudentRepository
import javax.inject.Inject

class StudentRepositoryImpl @Inject constructor(
    private val studentDao: StudentDao,
    private val api: StudentApi, // 现在注入真实的 Retrofit 接口
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : StudentRepository {

    private val _allStudents = studentDao.getAllStudents()
        .map { entities -> entities.map { it.toDomain() } }
        .flowOn(ioDispatcher)
        .stateIn(
            scope = GlobalScope, // 或者是跟应用生命周期绑定的 Scope
            started = SharingStarted.Eagerly, // 只要应用在运行，就保持最新并共享
            initialValue = emptyList()
        )

    override fun getStudents(): Flow<List<Student>> {
        return _allStudents
    }

    override suspend fun syncFromRemote() = withContext(ioDispatcher) {
        try {
            val response = api.getStudents()
            if (response.isSuccessful) {
                // 成功：将 DTO 转为 Entity 并存入 Room
                val entities = response.body()?.map { it.toEntity() } ?: emptyList()
                studentDao.insertAll(entities)
            } else {
                // 失败：可以在这里抛异常或处理 Mock 逻辑
                handleMockData()
            }
        } catch (e: Exception) {
            // 网络断开等异常情况，回退到 Mock
            handleMockData()
        }
    }

    private suspend fun handleMockData() {
        if (studentDao.getCount() == 0) {
            val mocks = (1..10000).map { i ->
                Student(
                    id = "id_$i",
                    name = "Student_$i",
                    gender = if (i % 2 == 0) "Male" else "Female",
                    birthday = System.currentTimeMillis() - (15L..25L).random() * 31536000000L,
                    height = (155..190).random(),
                    isLocked = true
                )
            }.map { it.toEntity() }
            studentDao.insertAll(mocks)
        }
    }

    override suspend fun updateLockStatus(id: String, isLocked: Boolean) {
        studentDao.updateLockStatus(id, isLocked)
    }
}