package miao.kmirror.miaolibrary.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import miao.kmirror.miaolibrary.demo.ui.theme.MiaoLibTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiaoLibTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(
                            text = "弹出 Compose Dialog",
                            modifier = Modifier
                                .clickable {
                                    val composeSampleDialog = ComposeSampleDialog()
                                    composeSampleDialog.show(this@MainActivity.supportFragmentManager, "miao")
                                }
                        )

                        Text(
                            text = "弹出 Xml Dialog",
                            modifier = Modifier
                                .clickable {
                                    val viewSampleDialog = ViewSampleDialog()
                                    viewSampleDialog.show(this@MainActivity.supportFragmentManager, "miao_view")
                                }
                        )
                    }

                }
            }
        }
    }

}
