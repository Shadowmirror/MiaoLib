package miao.kmirror.miaolibrary.demo

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import miao.kmirror.miaolibrary.demo.databinding.DialogTestBinding
import miao.kmirror.miaolibrary.ui.BaseDialogFragment
import miao.kmirror.miaolibrary.ui.DialogConfig


class ComposeSampleViewModel : ViewModel() {
    private val _message = MutableStateFlow("你好")
    val mMessage: StateFlow<String> = _message


    fun addText() {
        _message.value += "喵"
    }
}

class ViewSampleDialog : BaseDialogFragment<DialogTestBinding, ComposeSampleViewModel>() {

    override val config = DialogConfig(
        cancelableOutside = true,
        interceptBack = false,
        showStatusBar = false
    )

    override val viewModel by viewModels<ComposeSampleViewModel>()


    override fun getLayoutRes() = R.layout.dialog_test
    override fun initBinding(root: View) = DialogTestBinding.bind(root)

    override fun initViewBinding(binding: DialogTestBinding) {
        super.initViewBinding(binding)
    }

}


class ComposeSampleDialog : BaseDialogFragment<Nothing, ComposeSampleViewModel>() {

    override val config = DialogConfig(
        cancelableOutside = true,
        interceptBack = false,
    )

    override val viewModel by viewModels<ComposeSampleViewModel>()
    override fun initComposeView(): @Composable (() -> Unit) = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x50FF0000))
        ) {

        }
    }

}