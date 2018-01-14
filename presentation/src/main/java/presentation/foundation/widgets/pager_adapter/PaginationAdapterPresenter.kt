package presentation.foundation.widgets.pager_adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import data.foundation.PaginatedItems
import data.foundation.extentions.addTo
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import presentation.foundation.transformations.Transformations
import timber.log.Timber
import javax.inject.Inject

class PaginationAdapterPresenter<T> @Inject constructor(
        val timber: Timber.Tree,
        val transformations: Transformations) {

    private val cachedPages = mutableMapOf<String?, PaginatedItems<T>>()

    private var currentPage: Int? = null
    private var loadNextPageNeedToReset = false
    private var isLoading = false

    private lateinit var loader: (Int?) -> Single<PaginatedItems<T>>
    private lateinit var paginable: Paginable<T>
    private lateinit var disposeOnDestroy: CompositeDisposable
    private lateinit var consumerOnSuccess: Consumer<PaginatedItems<T>>

    //Used mainly for paginated queries
    private var key: String? = null

    fun init(loader: (Int?) -> Single<PaginatedItems<T>>,
             paginable: Paginable<T>,
             disposeOnDestroy: CompositeDisposable,
             preLoadNumber: Int = 20,
             consumerOnSuccess: Consumer<PaginatedItems<T>> = Consumer { },
             key: String? = null) {
        this.loader = loader
        this.paginable = paginable
        this.disposeOnDestroy = disposeOnDestroy
        this.consumerOnSuccess = consumerOnSuccess
        this.key = key

        paginable.setOnLoadMoreListener(BaseQuickAdapter.RequestLoadMoreListener { loadNextPage() })
        paginable.setPreLoadNumber(preLoadNumber)
        resetPagination()
    }

    fun loadNextPage() {
        if (isLoading) {
            return
        }

        isLoading = true

        loader(currentPage)
                .compose(readWithLoader())
                .compose(transformations.schedules())
                .doOnSuccess(consumerOnSuccess)
                .subscribe({ (offset, items, hasNext) ->
                    currentPage = offset

                    if (loadNextPageNeedToReset) {
                        paginable.replaceData(items)
                    } else {
                        paginable.addData(items)
                    }

                    isLoading = false
                    loadNextPageNeedToReset = false
                    paginable.loadMoreComplete()

                    if (!hasNext) {
                        paginable.loadMoreEnd()
                    }
                }, { e ->
                    timber.e(e)
                    paginable.loadMoreFail()
                }).addTo(disposeOnDestroy)
    }

    fun readWithLoader(): SingleTransformer<PaginatedItems<T>, PaginatedItems<T>> =
            SingleTransformer {
                val composedKey = "$currentPage$key"

                val cachedPage = cachedPages[composedKey]
                if (cachedPage != null) {
                    Single.just(cachedPages[composedKey])
                } else {
                    it.doOnSuccess { cachedPages[composedKey] = it }
                }
            }

    fun resetPagination() {
        currentPage = null
        loadNextPageNeedToReset = true
    }

    fun resetCache() {
        cachedPages.clear()
    }
}
