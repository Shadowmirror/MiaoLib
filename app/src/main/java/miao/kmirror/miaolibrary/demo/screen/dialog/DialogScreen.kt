package miao.kmirror.miaolibrary.demo.screen.dialog

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import miao.kmirror.miaolibrary.ui.DialogAnimatorDefault
import miao.kmirror.miaolibrary.ui.ViewDialog

@Composable
fun DialogScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        val context = LocalContext.current
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
                        val viewDialog = ViewDialog(
                            context = context,
                            showBgShadow = false,
                            animator = DialogAnimatorDefault.NoAnimation
                        )
                        viewDialog.showCompose {

                            var visible by remember { mutableStateOf(false) }

                            LaunchedEffect(Unit) {
                                visible = true
                            }

                            BackHandler(enabled = visible) {
                                visible = false
                            }

                            AnimatedVisibility(
                                visible = visible,
                                exit = fadeOut(),
                                enter = fadeIn()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0x7F000000))
                                )
                            }
                            AnimatedVisibility(
                                visible = visible,
                                exit = fadeOut() + scaleOut(),
                                enter = fadeIn() + scaleIn(),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .fillMaxWidth(0.8f)
                                            .heightIn(300.dp)
                                            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                                    ) {
                                        Text(
                                            text = "我是一个弹窗",
                                            modifier = Modifier
                                                .clickable {
                                                    visible = false
                                                }
                                        )
                                    }
                                }
                            }

                            LaunchedEffect(visible) {
                                if (!visible) {
                                    // 等动画结束
                                    delay(250)
                                    viewDialog.dismiss()
                                }
                            }

                        }
                    },
                    modifier = Modifier.fillMaxSize(0.8f)
                ) {
                    Text(
                        text = "展示弹窗"
                    )
                }

            }
        }
    }
}