package miao.kmirror.miaolibrary.demo.screen.main

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import miao.kmirror.miaolibrary.demo.DialogScreenNav

@Composable
fun MainScreen(
    backStack: SnapshotStateList<Any>
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(it)

        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        backStack.add(DialogScreenNav)
                    },
                    modifier = Modifier.fillMaxSize(0.8f)
                ) {
                    Text(
                        text = "Dialog 页"
                    )
                }
            }
        }
    }
}