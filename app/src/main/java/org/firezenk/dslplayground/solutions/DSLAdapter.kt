package org.firezenk.dslplayground.solutions

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

class DSLAdapter<M>(private val holderBuilder: HolderBuilder<M>,
                    comparator: Comparator<M>)
    : ListAdapter<M, DSLAdapter.ViewHolder<M>>(DiffCallback(comparator)) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<M>
            = holderBuilder.build(parent)

    override fun onBindViewHolder(holder: ViewHolder<M>, position: Int)
            = holder.bind(getItem(position))

    class ViewHolder<in M>(itemView: View, private val holderBuilder: HolderBuilder<M>)
        : RecyclerView.ViewHolder(itemView) {

        fun bind(vm: M) = with(holderBuilder) {
            onBind(itemView, vm)
            itemView.setOnClickListener { onClick?.invoke(vm) }
        }
    }
}

class DiffCallback<M>(private val comparator: Comparator<M>) : DiffUtil.ItemCallback<M>() {

    override fun areItemsTheSame(oldItem: M, newItem: M) = comparator.compare(oldItem, newItem) == 0

    override fun areContentsTheSame(oldItem: M, newItem: M) = oldItem == newItem
}