package presentation.foundation.widgets.pager_adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.loadmore.LoadMoreView
import org.base_app_android.R

class PaginationLoadMoreView : LoadMoreView() {
    override fun getLayoutId() = R.layout.paginated_load_more_view

    var initialHeight: Int? = null

    /**
     * Just a little hack to prevent showing view at the end of paginated results
     */
    override fun convert(holder: BaseViewHolder) {
        if (loadMoreStatus == LoadMoreView.STATUS_END) {
            if (initialHeight == null) {
                initialHeight = holder.itemView.height
            }
            holder.itemView.layoutParams.height = 0
        } else {
            initialHeight?.let { holder.itemView.layoutParams.height = it }
            super.convert(holder)
        }
    }

    /**
     * The view to show when next request is pending.
     */
    override fun getLoadingViewId() = R.id.flLoadingView

    /**
     * We don't show any view when the paginated requests end
     */
    override fun getLoadEndViewId() = R.id.flLoadEndViewId

    /**
     * We don't show any view when any paginated request fails
     * See loadMoreFail method below for more info.
     */
    override fun getLoadFailViewId() = R.id.flLoadFailView
}
