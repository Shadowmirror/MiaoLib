package miao.kmirror.miaolibrary.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import miao.kmirror.miaolibrary.demo.ui.theme.MiaoLibTheme
import miao.kmirror.miaolibrary.ktx.hideSystemBars
import miao.kmirror.miaolibrary.ui.ViewDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 调用扩展函数隐藏导航栏和状态栏
        hideSystemBars()
        
        setContent {
            MiaoLibTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Green),
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(20.dp)
                            .background(Color.Red)
                            .clickable {
                                val viewDialog = ViewDialog(this@MainActivity)
                                viewDialog.showCompose {
                                    Box(
                                        modifier = Modifier.fillMaxSize()
                                    ){
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .size(200.dp)
                                                .background(Color.Blue)
                                                .clickable{
                                                    viewDialog.dismiss()
                                                }
                                        )

                                    }
                                }
                                val viewDialog1 = ViewDialog(this@MainActivity)
                                viewDialog1.showCompose {
                                    Box(
                                        modifier = Modifier.fillMaxSize()
                                    ){
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .size(200.dp)
                                                .background(Color.Yellow)
                                                .clickable{
                                                    viewDialog1.dismiss()
                                                }
                                        )

                                    }
                                }

                            }
                    )
                }
            }
        }
    }
}
