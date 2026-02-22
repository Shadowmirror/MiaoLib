package miao.kmirror.miaolibrary.demo.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import miao.kmirror.miaolibrary.demo.domain.model.Student
import miao.kmirror.miaolibrary.demo.domain.repository.StudentRepository
import javax.inject.Inject

class GetStudentsUseCase @Inject constructor(
    private val repository: StudentRepository
) {
    operator fun invoke(): Flow<List<Student>> {
        return repository.getStudents().map { list ->
            // 业务排序逻辑：按名字首字母排序
            list.sortedBy { it.name }
        }
    }
}