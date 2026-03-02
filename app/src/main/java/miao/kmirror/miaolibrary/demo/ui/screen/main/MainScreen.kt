package miao.kmirror.miaolibrary.demo.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import miao.kmirror.miaolibrary.demo.R
import miao.kmirror.miaolibrary.demo.ui.DialogScreenNav
import miao.kmirror.miaolibrary.demo.ui.StudentScreenNav
import miao.kmirror.miaolibrary.demo.ui.view.UnifiedNinePatchView

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
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        backStack.add(StudentScreenNav)
                    },
                    modifier = Modifier.fillMaxSize(0.8f)
                ) {
                    Text(
                        text = "Student 页"
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                UnifiedNinePatchView(
                    drawableRes = R.drawable.img_btn_test,
                    leftSplit = 200,
                    rightSplit = 250,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp)
                )
                UnifiedNinePatchView(
                    drawableRes = R.drawable.img_btn_test_2,
                    topSplit = 200,
                    bottomSplit = 250,
                    modifier = Modifier
                        .width(22.dp)
                        .height(220.dp)
                )
                UnifiedNinePatchView(
                    drawableRes = R.drawable.img_btn_test,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp)
                )
                UnifiedNinePatchView(
                    drawableRes = R.drawable.img_btn_test,
                    leftSplit = 0,
                    topSplit = 0,
                    rightSplit = 492,
                    bottomSplit = 132,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp)
                )
                UnifiedNinePatchView(
                    drawableRes = R.drawable.img_btn_test,
                    leftSplit = 200,
                    topSplit = 64,
                    rightSplit = 250,
                    bottomSplit = 68,
                    modifier = Modifier
                        .width(204.dp)
                        .height(84.dp)
                )
            }
        }
    }
}