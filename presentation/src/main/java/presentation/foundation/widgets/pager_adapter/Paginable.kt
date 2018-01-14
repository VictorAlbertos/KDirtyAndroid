package presentation.foundation.widgets.pager_adapter

import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * An indirection layer to prevent coupling PaginationAdapterPresenter with PaginationAdapter.
 */
interface Paginable<T> {
    fun replaceData(data: Collection<T>)
    fun addData(data: Collection<T>)
    fun addData(position: Int, data: T)

    fun setOnLoadMoreListener(requestLoadMoreListener: BaseQuickAdapter.RequestLoadMoreListener)

    fun setPreLoadNumber(preLoadNumber: Int)

    fun loadMoreComplete()
    fun loadMoreFail()
    fun loadMoreEnd()

    fun notifyDataSetChanged()

    fun getData(): MutableList<T>
}
