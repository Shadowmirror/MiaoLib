package miao.kmirror.miaolibrary.demo.ui.screen.student

data class TreeItem<T>(
    val data: T?,
    val id: String,
    val title: String = "",
    val isHeader: Boolean = false,
    val parentId: String? = null,
    val isExpanded: Boolean = true
)