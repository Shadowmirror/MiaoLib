package miao.kmirror.miaolibrary.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import miao.kmirror.miaolibrary.demo.screen.dialog.DialogScreen
import miao.kmirror.miaolibrary.demo.screen.main.MainScreen
import miao.kmirror.miaolibrary.demo.ui.theme.MiaoLibTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 调用扩展函数隐藏导航栏和状态栏
        setContent {
            MiaoLibTheme {
                val backStack = remember { mutableStateListOf<Any>(MainScreenNav) }
                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = { key ->
                        when (key) {
                            is MainScreenNav -> NavEntry(key) {
                                MainScreen(backStack)
                            }

                            is DialogScreenNav -> NavEntry(key) {
                                DialogScreen()
                            }

                            else -> error("Unknown key: $key")
                        }
                    },
                    transitionSpec = {
                        ContentTransform(
                            targetContentEnter = scaleIn(
                                initialScale = 0.8f,
                                animationSpec = tween(300)
                            ) + fadeIn(
                                animationSpec = tween(300)
                            ),
                            initialContentExit = ExitTransition.None // A 页面不动
                        )
                    },

                    popTransitionSpec = {
                        ContentTransform(
                            targetContentEnter = EnterTransition.None, // A 页面不动
                            initialContentExit = scaleOut(
                                targetScale = 0.8f,
                                animationSpec = tween(300)
                            ) + fadeOut(
                                animationSpec = tween(300)
                            )
                        )
                    },

                    predictivePopTransitionSpec = {
                        ContentTransform(
                            targetContentEnter = EnterTransition.None, // A 页面不动
                            initialContentExit = scaleOut(
                                targetScale = 0.8f,
                                animationSpec = tween(300)
                            ) + fadeOut(
                                animationSpec = tween(300)
                            )
                        )
                    }
                )
            }
        }
    }
}

@Serializable
data object MainScreenNav : NavKey

@Serializable
data object DialogScreenNav : NavKey
