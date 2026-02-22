package miao.kmirror.miaolibrary.demo.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import miao.kmirror.miaolibrary.demo.ui.screen.student.TreeItem

// ui/components/GenericTreeGrid.kt
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> GenericTreeGrid(
    items: List<TreeItem<T>>,
    modifier: Modifier = Modifier,
    headerContent: @Composable (TreeItem<T>) -> Unit,
    itemContent: @Composable (T) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = items,
            key = { it.id }, // 必须设置 key 才能有平移动画
            span = { item ->
                // Header 占据全部 2 列，普通 Item 占据 1 列
                GridItemSpan(if (item.isHeader) 2 else 1)
            }
        ) { item ->
            Box(
//                modifier = Modifier.animateItem()
            ) { // 维度切换时的飞入动画
                if (item.isHeader) {
                    headerContent(item)
                } else {
                    item.data?.let { itemContent(it) }
                }
            }
        }
    }
}