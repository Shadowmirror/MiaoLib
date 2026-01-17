package miao.kmirror.miaolibrary.ui

import androidx.compose.runtime.Composable
import miao.kmirror.miaolibrary.ui.DialogThemeConfig.themeProvider

// 全局对话框主题配置器
object DialogThemeConfig {

    // 默认主题配置（无额外主题包装）
    internal var themeProvider: (@Composable (content: @Composable () -> Unit) -> Unit) = { content -> content() }

    /**
     * 设置全局对话框主题
     * @param themeProvider 主题包装器，用于包装对话框内容
     */
    fun setDialogThemeProvider(provider: @Composable (content: @Composable () -> Unit) -> Unit) {
        themeProvider = provider
    }

    /**
     * 重置为默认（无主题）
     */
    fun reset() {
        themeProvider = { content -> content() }
    }
}