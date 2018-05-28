package org.firezenk.dslplayground

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import org.firezenk.dslplayground.solutions.DSLAdapter

class RecyclerItemTouchHelper(dragDirs: Int, swipeDirs: Int,
                              private val listener: Listener)
    : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean = true

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            (viewHolder as DSLAdapter.ViewHolder<*>).swipeView.run {
                ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(this)
            }
        }
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView,
                                 viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                                 actionState: Int, isCurrentlyActive: Boolean) {
        (viewHolder as DSLAdapter.ViewHolder<*>).swipeView.run {
            ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(c, recyclerView, this, dX,
                    dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
        (viewHolder as DSLAdapter.ViewHolder<*>).swipeView.run {
            ItemTouchHelper.Callback.getDefaultUIUtil().clearView(this)
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                             actionState: Int, isCurrentlyActive: Boolean) {
        (viewHolder as DSLAdapter.ViewHolder<*>).swipeView.run {
            ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, this, dX, dY,
                    actionState, isCurrentlyActive)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
            = listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)

    interface Listener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    }
}
