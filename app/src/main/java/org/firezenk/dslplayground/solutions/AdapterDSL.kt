package org.firezenk.dslplayground.solutions

import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.firezenk.dslplayground.RecyclerItemTouchHelper

@DslMarker
annotation class AdapterDSL

@AdapterDSL
class SwipeBuilder(private val recyclerView: RecyclerView) {

    var dragDirs: Int = 0
    var swipeDirs: Int = 0
    lateinit var listener: RecyclerItemTouchHelper.Listener

    fun build() {
        val itemTouchHelperCallback = RecyclerItemTouchHelper(dragDirs, swipeDirs, listener)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
    }
}

@AdapterDSL
class HolderBuilder<M>(private val recyclerView: RecyclerView, @LayoutRes val layout: Int) {

    lateinit var onBind: (View, M) -> Unit
    var onClick: ((M) -> Unit)? = null

    private var swipeViewId: Int = 0

    fun bindView(function: (View, M) -> Unit) {
        this.onBind = function
    }

    fun click(function: (M) -> Unit) {
        this.onClick = function
    }

    fun swipe(@IdRes swipeViewId: Int = 0, setup: SwipeBuilder.() -> Unit) {
        this.swipeViewId = swipeViewId
        SwipeBuilder(recyclerView).apply(setup).build()
    }

    fun build(parent: ViewGroup): DSLAdapter.ViewHolder<M> {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        return DSLAdapter.ViewHolder(
                inflater.inflate(layout, parent, false), swipeViewId, this)
    }
}

@AdapterDSL
class AdapterDslBuilder<M>(private val recyclerView: RecyclerView) {

    private lateinit var holderBuilder: HolderBuilder<M>
    private lateinit var comparator: Comparator<M>

    fun build(): DSLAdapter<M> = DSLAdapter(holderBuilder, comparator)

    fun item(@LayoutRes layout: Int = 0, setup: HolderBuilder<M>.() -> Unit) {
        this.holderBuilder = HolderBuilder<M>(recyclerView, layout).apply(setup)
    }

    fun diff(comparator: (o1: M) -> Int) {
        this.comparator = compareBy { comparator(it) }
    }
}

@AdapterDSL
fun <M> adapterDSL(recyclerView: RecyclerView, itemDecoration: DividerItemDecoration,
                   setup: AdapterDslBuilder<M>.() -> Unit): DSLAdapter<M> =
        with(AdapterDslBuilder<M>(recyclerView)) {
            setup()
            build().also {
                recyclerView.adapter = it
                recyclerView.addItemDecoration(itemDecoration)
            }
        }