package miao.kmirror.miaolibrary.demo.domain.repository

import kotlinx.coroutines.flow.Flow
import miao.kmirror.miaolibrary.demo.domain.model.Student

interface StudentRepository {
    /**
     * 获取学生流。实现类应处理本地缓存逻辑。
     */
    fun getStudents(): Flow<List<Student>>

    /**
     * 更新学生锁定状态
     */
    suspend fun updateLockStatus(id: String, isLocked: Boolean)

    /**
     * 强制从网络同步数据到本地
     */
    suspend fun syncFromRemote()
}