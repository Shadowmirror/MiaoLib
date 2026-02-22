package miao.kmirror.miaolibrary.demo.ui.screen.student

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import miao.kmirror.miaolibrary.demo.domain.model.Student
import miao.kmirror.miaolibrary.demo.ui.component.GenericTreeGrid

@Composable
fun StudentScreen(viewModel: StudentViewModel = hiltViewModel()) {
    val nodes by viewModel.uiNodes.collectAsStateWithLifecycle()
    val currentType by viewModel.groupType.collectAsState()

    Column(Modifier.fillMaxSize()) {
        // 1. 维度切换 Tab
        ScrollableTabRow(selectedTabIndex = currentType.ordinal) {
            GroupType.values().forEach { type ->
                Tab(
                    selected = currentType == type,
                    onClick = { viewModel.groupType.value = type },
                    text = { Text(type.name) }
                )
            }
        }

        // 2. 高性能列表
        GenericTreeGrid(
            items = nodes,
            modifier = Modifier.weight(1f),
            headerContent = { header ->
                Surface(
                    onClick = { viewModel.toggleHeader(header.id) },
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = header.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.weight(1f))
                        val rotation by animateFloatAsState(if (header.isExpanded) 180f else 0f)
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Collapse/Expand",
                            modifier = Modifier.rotate(rotation)
                        )
                    }
                }
            },
            itemContent = { student ->
                StudentCard(
                    student = student,
                    onToggleLock = { viewModel.toggleLock(student.id, student.isLocked) }
                )
            }
        )
    }
}

@Composable
fun StudentCard(student: Student, onToggleLock: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onToggleLock
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(student.name, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = if (student.isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                    contentDescription = null,
                    tint = if (student.isLocked) Color.Red else Color.Green
                )
            }
            Text("年龄: ${student.age} | 身高: ${student.height}cm", style = MaterialTheme.typography.bodySmall)
        }
    }
}
