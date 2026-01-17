package miao.kmirror.miaolibrary.ktx_compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

/**
 * Compose 组件点击拓展函数
 * */
fun Modifier.noRippleDebounceClickable(
    intervalMs: Long = 500L,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    var lastClickTime by remember { mutableLongStateOf(0L) }

    clickable(
        interactionSource = interactionSource,
        indication = null
    ) {
        val now = System.currentTimeMillis()
        if (now - lastClickTime >= intervalMs) {
            lastClickTime = now
            onClick()
        }
    }
}

val LocalScaleFactor = compositionLocalOf { 1f }

enum class UIAdaptiveMode {
    Width,   // 默认，按设计稿宽度
    Height,  // 按设计稿高度
    Min,     // min(width, height)，保证完整比例
    None     // 不参与任何适配, 就是默认 DP 值
}

@Composable
fun UIAdaptive(
    mode: UIAdaptiveMode = UIAdaptiveMode.Width,
    designWidthDp: Float = 360f,
    designHeightDp: Float = 640f,
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.toFloat()
    val screenHeightDp = configuration.screenHeightDp.toFloat()

    val scaleFactor = remember(
        mode,
        screenWidthDp,
        screenHeightDp,
        designWidthDp,
        designHeightDp
    ) {
        when (mode) {
            UIAdaptiveMode.Width ->
                screenWidthDp / designWidthDp

            UIAdaptiveMode.Height ->
                screenHeightDp / designHeightDp

            UIAdaptiveMode.Min ->
                min(
                    screenWidthDp / designWidthDp,
                    screenHeightDp / designHeightDp
                )

            UIAdaptiveMode.None ->
                1f
        }
        // 可选安全边界,安全边界暂不考虑，我们的 UI 暂时没到适配折叠屏的地步
//            .coerceIn(0.85f, 1.3f)
    }

    CompositionLocalProvider(
        LocalScaleFactor provides scaleFactor
    ) {
        content()
    }
}

/**
 * 这个是不需要 real 的方案
 * */
@Composable
fun UIAdaptiveWithoutReal(
    mode: UIAdaptiveMode = UIAdaptiveMode.Width,
    designWidthDp: Float = 360f,
    designHeightDp: Float = 640f,
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthDp = configuration.screenWidthDp.toFloat()
    val screenHeightDp = configuration.screenHeightDp.toFloat()

    val scaleFactor = remember(
        mode,
        screenWidthDp,
        screenHeightDp,
        designWidthDp,
        designHeightDp
    ) {
        when (mode) {
            UIAdaptiveMode.Width -> screenWidthDp / designWidthDp
            UIAdaptiveMode.Height -> screenHeightDp / designHeightDp
            UIAdaptiveMode.Min -> min(
                screenWidthDp / designWidthDp,
                screenHeightDp / designHeightDp
            )

            UIAdaptiveMode.None -> 1f
        }
    }

    // 自定义 Density
    val customDensity = remember(scaleFactor) {
        Density(density = density.density * scaleFactor)
    }

    CompositionLocalProvider(
        LocalDensity provides customDensity,
    ) {
        content()
    }
}

@Composable
fun real(designDp: Dp): Dp {
    return (designDp.value * LocalScaleFactor.current).dp
}

@Composable
fun real(designSp: TextUnit): TextUnit {
    return (designSp.value * LocalScaleFactor.current).sp
}
