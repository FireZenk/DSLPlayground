package org.firezenk.dslplayground.solutions

import android.support.annotation.LayoutRes
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

@DslMarker
annotation class AdapterDSL

@AdapterDSL
class HolderBuilder<M>(@LayoutRes val layout: Int) {

    lateinit var onBind: (View, M) -> Unit
    var onClick: ((M) -> Unit)? = null

    fun bindView(function: (View, M) -> Unit) {
        this.onBind = function
    }

    fun click(function: (M) -> Unit) {
        this.onClick = function
    }

    fun build(parent: ViewGroup): DSLAdapter.ViewHolder<M> {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        return DSLAdapter.ViewHolder(inflater.inflate(layout, parent, false), this)
    }
}

@AdapterDSL
class AdapterDslBuilder<M> {

    private lateinit var holderBuilder: HolderBuilder<M>
    private lateinit var comparator: Comparator<M>

    fun build(): DSLAdapter<M> = DSLAdapter(holderBuilder, comparator)

    fun item(@LayoutRes layout: Int = 0, setup: HolderBuilder<M>.() -> Unit) {
        this.holderBuilder = HolderBuilder<M>(layout).apply(setup)
    }

    fun diff(comparator: (o1: M) -> Int) {
        this.comparator = compareBy { comparator(it) }
    }
}

@AdapterDSL
fun <M> adapterDSL(recyclerView: RecyclerView, itemDecoration: DividerItemDecoration,
                   setup: AdapterDslBuilder<M>.() -> Unit): DSLAdapter<M> =
        with(AdapterDslBuilder<M>()) {
            setup()
            build().also {
                recyclerView.adapter = it
                recyclerView.addItemDecoration(itemDecoration)
            }
        }