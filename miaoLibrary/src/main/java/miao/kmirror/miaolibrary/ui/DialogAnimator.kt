package miao.kmirror.miaolibrary.ui

import android.view.View

interface DialogAnimator {
    fun animateEnter(content: View, overlay: View, end: (() -> Unit)? = null)
    fun animateExit(content: View, overlay: View, end: (() -> Unit)? = null)

}

object DialogAnimatorDefault {
    object ScaleAlphaFromCenterAnimator : DialogAnimator {
        override fun animateEnter(content: View, overlay: View, end: (() -> Unit)?) {
            overlay.alpha = 0f
            content.alpha = 0f
            content.scaleX = 0.9f
            content.scaleY = 0.9f

            overlay.animate()
                .alpha(1f)
                .setDuration(200)
                .start()

            content.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(250)
                .setInterpolator(android.view.animation.DecelerateInterpolator())
                .withEndAction { end?.invoke() }
                .start()
        }

        override fun animateExit(content: View, overlay: View, end: (() -> Unit)?) {
            content.animate()
                .alpha(0f)
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(200)
                .setInterpolator(android.view.animation.AccelerateInterpolator())
                .start()

            overlay.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction { end?.invoke() }
                .start()
        }
    }

    object TranslateAlphaFromBottomAnimator : DialogAnimator {

        override fun animateEnter(content: View, overlay: View, end: (() -> Unit)?) {
            overlay.alpha = 0f
            content.alpha = 0f

            content.post {
                content.translationY = content.height.toFloat()

                overlay.animate()
                    .alpha(1f)
                    .setDuration(200)
                    .start()

                content.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setDuration(300)
                    .setInterpolator(android.view.animation.DecelerateInterpolator())
                    .withEndAction { end?.invoke() }
                    .start()
            }
        }

        override fun animateExit(content: View, overlay: View, end: (() -> Unit)?) {
            content.animate()
                .translationY(content.height.toFloat())
                .alpha(0f)
                .setDuration(250)
                .setInterpolator(android.view.animation.AccelerateInterpolator())
                .start()

            overlay.animate()
                .alpha(0f)
                .setDuration(250)
                .withEndAction { end?.invoke() }
                .start()
        }
    }


    object TranslateFromBottomAnimator : DialogAnimator {

        override fun animateEnter(content: View, overlay: View, end: (() -> Unit)?) {
            overlay.alpha = 1f

            content.post {
                content.translationY = content.height.toFloat()
                content.animate()
                    .translationY(0f)
                    .setDuration(300)
                    .setInterpolator(android.view.animation.DecelerateInterpolator())
                    .withEndAction { end?.invoke() }
                    .start()
            }
        }

        override fun animateExit(content: View, overlay: View, end: (() -> Unit)?) {
            content.animate()
                .translationY(content.height.toFloat())
                .setDuration(250)
                .setInterpolator(android.view.animation.AccelerateInterpolator())
                .withEndAction { end?.invoke() }
                .start()
        }
    }


    object NoAnimation : DialogAnimator {
        override fun animateEnter(content: View, overlay: View, end: (() -> Unit)?) {
            end?.invoke()
        }

        override fun animateExit(content: View, overlay: View, end: (() -> Unit)?) {
            end?.invoke()
        }
    }

}



