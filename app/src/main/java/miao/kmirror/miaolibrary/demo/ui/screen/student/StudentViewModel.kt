package miao.kmirror.miaolibrary.demo.ui.screen.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import miao.kmirror.miaolibrary.demo.di.DefaultDispatcher
import miao.kmirror.miaolibrary.demo.domain.model.Student
import miao.kmirror.miaolibrary.demo.domain.repository.StudentRepository
import miao.kmirror.miaolibrary.demo.domain.usecase.GetStudentsUseCase
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val getStudentsUseCase: GetStudentsUseCase,
    private val repository: StudentRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher // 用于计算密集型任务
) : ViewModel() {

    // 当前分组维度
    val groupType = MutableStateFlow(GroupType.GENDER)

    // 保存 Header 的展开/收起状态
    private val _expandedState = MutableStateFlow<Map<String, Boolean>>(emptyMap())

    // 组合数据流
    val uiNodes = combine(
        getStudentsUseCase(),
        groupType,
        _expandedState
    ) { students, type, expandedMap ->
        withContext(defaultDispatcher) {
            val result = mutableListOf<TreeItem<Student>>()

            // 1. 根据维度进行分组
            val grouped = when (type) {
                GroupType.GENDER -> students.groupBy { it.gender }
                GroupType.AGE -> students.groupBy { "${it.age} 岁" }
                GroupType.HEIGHT -> students.groupBy { "${(it.height / 5) * 5}cm 档" }
            }

            // 2. 将 Map 平铺成 List
            grouped.forEach { (groupName, list) ->
                val hId = "h_$groupName"
                val isExpanded = expandedMap[hId] ?: true // 默认展开
                // 添加组头
                result.add(
                    TreeItem(
                        data = null,
                        id = hId,
                        title = groupName,
                        isHeader = true,
                        isExpanded = isExpanded
                    )
                )
                // 如果是展开状态，才添加组成员
                if (isExpanded) {
                    list.forEach { student ->
                        result.add(TreeItem(data = student, id = student.id, parentId = hId))
                    }
                }
            }
            result
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // 旋转屏幕不断开
        initialValue = emptyList()
    )

    fun toggleLock(id: String, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.updateLockStatus(id, !currentStatus)
        }
    }

    fun toggleHeader(headerId: String) {
        val newMap = _expandedState.value.toMutableMap()
        newMap[headerId] = !(newMap[headerId] ?: true)
        _expandedState.value = newMap
    }
}

enum class GroupType { GENDER, AGE, HEIGHT }
