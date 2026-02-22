package miao.kmirror.miaolibrary.demo.ui.screen.dialog

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import miao.kmirror.miaolibrary.ui.DialogAnimatorDefault
import miao.kmirror.miaolibrary.ui.ViewDialog
import kotlin.math.roundToInt

@Composable
fun DialogScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        var isShowDialog1 by remember { mutableStateOf(false) }
        var isShowDialog2 by remember { mutableStateOf(false) }
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(it)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                ButtonItem("展示 Compose 自定义动画弹窗") {
                    isShowDialog1 = true
                }
                Spacer(modifier = Modifier.height(20.dp))
                ButtonItem("展示 Compose 底部弹窗") {
                    isShowDialog2 = true
                }
            }
        }

        if (isShowDialog1) {
            ViewDialog.ComposeDialog(
                showBgShadow = false,
                animator = DialogAnimatorDefault.NoAnimation,
                onDismiss = { isShowDialog1 = false }
            ) {
                ComposeAniEnterDialog { isShowDialog1 = false }
            }
        }

        if (isShowDialog2) {
            ViewDialog.ComposeDialog(
                showBgShadow = false,
                animator = DialogAnimatorDefault.NoAnimation,
                onDismiss = { isShowDialog2 = false }
            ) {
                ComposeBottomSheetDialog() { isShowDialog2 = false }
            }
        }
    }
}

@Composable
private fun ButtonItem(text: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.fillMaxSize(0.8f)
    ) {
        Text(text = text)
    }
}


@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeBottomSheetDialog(
    onDismiss: () -> Unit = {},
) {
    val isInPreview = LocalInspectionMode.current
    val scope = rememberCoroutineScope()
    var sheetHeight = 0f // Sheet 高度
    var positionalThresholdPx = with(LocalDensity.current) { (56.dp).toPx() }
    val velocityThresholdPx = with(LocalDensity.current) { (125.dp).toPx() }
    val windowInfo = LocalWindowInfo.current
    val screenHeight = windowInfo.containerSize.height
    // Animatable 方便平滑动画
    val sheetOffset = remember { Animatable(screenHeight.toFloat()) }
    val bgAlpha = remember { Animatable(0f) }
    // 是否显示
    var visible by remember { mutableStateOf(if (isInPreview) true else false) }

    LaunchedEffect(Unit) {
        visible = true
        launch {
            sheetOffset.animateTo(targetValue = 0f, animationSpec = tween(300))
        }
        launch {
            bgAlpha.animateTo(targetValue = 1f, animationSpec = tween(300))
        }
    }
    BackHandler(enabled = visible) {
        visible = false
    }

    // 背景点击关闭
    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(bgAlpha.value)
            .background(Color(0x7F000000))
            .pointerInput(Unit) {
                detectTapGestures { visible = false }
            }
    )
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(300.dp)
                .offset { IntOffset(0, sheetOffset.value.roundToInt()) }
                .onGloballyPositioned { layoutCoordinates ->
                    sheetHeight = layoutCoordinates.size.height.toFloat() // px
                }
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            val offset = (sheetOffset.value + delta).coerceIn(0f, sheetHeight)
                            sheetOffset.snapTo(offset)
                            bgAlpha.snapTo(1 - offset / sheetHeight)
                        }
                    },
                    onDragStopped = { velocity ->
                        scope.launch {
                            val shouldCloseByVelocity = velocity > velocityThresholdPx
                            val shouldCloseByPosition = sheetOffset.value > positionalThresholdPx
                            val shouldClose = shouldCloseByVelocity || shouldCloseByPosition
                            if (shouldClose) {
                                // 动画关闭
                                visible = false
                            } else {
                                // 回弹到顶部
                                sheetOffset.animateTo(targetValue = 0f, animationSpec = tween(250))
                                bgAlpha.animateTo(1f, tween(250))
                            }
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "我是一个 BottomSheet 弹窗",
                modifier = Modifier.clickable { visible = false }
            )
        }
    }

    // 监听 visible 变化，当 false 时调用 onDismiss
    LaunchedEffect(visible) {
        if (!visible) {
            launch {
                sheetOffset.animateTo(
                    targetValue = sheetHeight,
                    animationSpec = tween(250)
                )
            }
            launch {
                bgAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(250)
                )
            }
            launch {
                delay(250)
                onDismiss()
            }
        }
    }
}


/**
 * Compose 自定义动画，常用于进场动画和退场动画需要自定义，且和内部元素有联动部分
 * */
@Composable
private fun ComposeAniEnterDialog(onDismiss: () -> Unit) {
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
            onDismiss()
        }
    }
}