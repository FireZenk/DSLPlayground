package org.firezenk.dslplayground.solutions

import android.support.design.widget.BottomNavigationView

@DslMarker
annotation class BottomBarDSL

@BottomBarDSL
class MenuItemBuilder {

    lateinit var action: () -> Unit
    var id: Int = 0

    fun build() = id to action
}

@BottomBarDSL
class BottomBarBuilder(private val target: BottomNavigationView) {

    var menu: Int? = null
    var default: Int? = null

    private var items = mutableListOf<Pair<Int, () -> Unit>>()

    fun build(): BottomNavigationView {
        menu?.let { target.inflateMenu(it) }
        default?.let { target.selectedItemId = it }

        target.setOnNavigationItemSelectedListener {
            items.firstOrNull { item -> it.itemId == item.first }
                    ?.second
                    ?.invoke()
            true
        }

        return target
    }

    fun item(id: Int = 0, setup: MenuItemBuilder.() -> Unit) = MenuItemBuilder().apply {
        this.id = id
        setup()
    }.build()

    operator fun Pair<Int, () -> Unit>.unaryPlus() = items.add(this)
}

fun BottomNavigationView.dsl(setup: BottomBarBuilder.() -> Unit) {
    with(BottomBarBuilder(this)) {
        setup()
        build()
    }
}