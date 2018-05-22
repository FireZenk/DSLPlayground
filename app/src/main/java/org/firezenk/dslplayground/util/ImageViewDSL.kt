package org.firezenk.dslplayground.util

import android.widget.ImageView
import com.bumptech.glide.Glide

@DslMarker
annotation class ImageViewDsl

@ImageViewDsl
class ImageViewBuilder(val imageView: ImageView) {

    lateinit var url: String

    fun build() {
        Glide.with(imageView.context)
                .load(url)
                .into(imageView)
    }
}

fun ImageView.dsl(setup: ImageViewBuilder.() -> Unit) {
    with(ImageViewBuilder(this)) {
        setup()
        build()
    }
}