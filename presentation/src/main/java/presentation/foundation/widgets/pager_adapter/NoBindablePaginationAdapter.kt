package presentation.foundation.widgets.pager_adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * A bridge between BaseQuickAdapter lib and our own needs. BaseQuickAdapter limits the number of ViewHolder per adapter to 1
 * in exchange of a more sugar api. This bridge-adapter allow us to operate BaseQuickAdapter with raw ViewHolder.
 *
 * Also implement Paginable, the interface required by PaginationAdapterPresenter
 */
abstract class NoBindablePaginationAdapter<T> : BaseQuickAdapter<T, BaseViewHolder>(0), Paginable<T> {

    init {
        setLoadMoreView(PaginationLoadMoreView())
    }

    override fun convert(viewHolder: BaseViewHolder, item: T) = onBindActualViewHolder(viewHolder, item)

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int) = onCreateActualViewHolder(parent, viewType)

    override fun getDefItemViewType(position: Int): Int = getActualItemViewType(position)

    /**
     * Override to create the BaseViewHolder(s).
     */
    abstract fun onCreateActualViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder

    /**
     * Override to bind the BaseViewHolder(s).
     */
    abstract fun onBindActualViewHolder(viewHolder: BaseViewHolder, item: T)

    /**
     * Override if you need to provide more than one viewType. The position is already adjusted taking into account
     * potential header, footer and loadMore viewTypes.
     */
    open fun getActualItemViewType(position: Int): Int = super.getDefItemViewType(position)
}