package presentation.foundation.widgets.pager_adapter

import android.support.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Implement Paginable, the interface required by PaginationAdapterPresenter
 */
abstract class PaginationAdapter<T, K : BaseViewHolder>(@LayoutRes layoutResId: Int) : BaseQuickAdapter<T, K>(layoutResId), Paginable<T> {

    init {
        setLoadMoreView(PaginationLoadMoreView())
    }
}
