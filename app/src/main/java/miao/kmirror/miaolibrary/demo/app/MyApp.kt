package miao.kmirror.miaolibrary.demo.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import miao.kmirror.miaolibrary.demo.ui.theme.MiaoLibTheme
import miao.kmirror.miaolibrary.ui.DialogThemeConfig

@HiltAndroidApp
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