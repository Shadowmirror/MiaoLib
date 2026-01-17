package miao.kmirror.miaolibrary.demo

import android.app.Application
import miao.kmirror.miaolibrary.demo.ui.theme.MiaoLibTheme
import miao.kmirror.miaolibrary.ui.DialogThemeConfig

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        DialogThemeConfig.setDialogThemeProvider { content ->
            MiaoLibTheme {
                content()
            }
        }
    }
}