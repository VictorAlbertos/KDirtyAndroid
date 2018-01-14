package presentation.foundation.widgets.pager_adapter

import com.nhaarman.mockito_kotlin.*
import data.foundation.PaginatedItems
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import presentation.foundation.transformations.Transformations
import presentation.sections.TransformationsMock
import timber.log.Timber

class PaginationAdapterPresenterTest {
    private val transformations: Transformations = spy(TransformationsMock())
    private val logger = mock<Timber.Tree>()
    private val paginable = mock<Paginable<MockModel>>()
    private val disposeOnDestroy = CompositeDisposable()
    private val paginatedItems = mock<PaginatedItems<MockModel>>()
    private val items = listOf<MockModel>()
    private var currentPage: Int? = null
    private val preloadNumber = 20
    private var loaderFails = false
    private var error = RuntimeException()

    val presenterUT by lazy {
        val paginationAdapterPresenter = PaginationAdapterPresenter<MockModel>(logger, transformations)
        paginationAdapterPresenter.init({
            currentPage = it
            if (loaderFails) {
                Single.error(error)
            } else {
                Single.just(paginatedItems)
            }
        }, paginable, disposeOnDestroy, preloadNumber)
        paginationAdapterPresenter
    }

    @Test
    fun verifyInit() {
        presenterUT.loadNextPage()

        verify(paginable, times(1)).setOnLoadMoreListener(any())
        verify(paginable, times(1)).setPreLoadNumber(preloadNumber)
    }

    @Test
    fun verifyLoadNextPageHasNext() {
        val paginationAdapterPresenter = PaginationAdapterPresenter<MockModel>(logger, transformations)
        paginationAdapterPresenter.init({ Single.just(PaginatedItems(1, listOf(), true)) }, paginable, disposeOnDestroy, preloadNumber)

        paginationAdapterPresenter.loadNextPage()

        verify(paginable, never()).loadMoreEnd()
    }

    @Test
    fun verifyLoadNextPageHasNotNext() {
        whenever(paginatedItems.hasNext).thenReturn(false)

        presenterUT.loadNextPage()

        verify(paginable, times(1)).loadMoreEnd()
    }

    @Test
    fun verifyLoadNextPage() {
        val offset = 0

        presenterUT.loadNextPage()

        verify(paginable, times(1)).loadMoreComplete()
        verify(paginable, times(1)).replaceData(items)

        assertThat(currentPage).isNull()

        presenterUT.loadNextPage()

        verify(paginable, times(2)).loadMoreComplete()
        verify(paginable, times(1)).addData(items)

        assertThat(currentPage).isEqualTo(offset)

        //it is cached that's why loader can crash and still get the data
        loaderFails = true

        presenterUT.resetPagination()
        presenterUT.loadNextPage()

        verify(paginable, times(3)).loadMoreComplete()
        verify(paginable, times(2)).replaceData(items)

        //now we reset the cache and we get the error from the loader

        presenterUT.resetPagination()
        presenterUT.resetCache()
        presenterUT.loadNextPage()

        verify(paginable, times(3)).loadMoreComplete()
        verify(paginable, times(2)).replaceData(items)

        verify(paginable, times(1)).loadMoreFail()
        verify(logger, times(1)).e(error)
    }

    @Test
    fun verifyLoadNextPageReset() {
        presenterUT.resetPagination()
        presenterUT.loadNextPage()

        verify(paginable, times(1)).loadMoreComplete()
        verify(paginable, times(1)).replaceData(items)
    }

    @Test
    fun verifyLoadNextPageIsLoading() {
        val presenterUT = PaginationAdapterPresenter<MockModel>(logger, transformations)
        presenterUT.init({ Single.never() }, paginable, disposeOnDestroy, preloadNumber)

        presenterUT.loadNextPage()
        presenterUT.loadNextPage()
        presenterUT.loadNextPage()
        presenterUT.loadNextPage()

        verify(paginable, never()).loadMoreComplete()
        verify(paginable, never()).addData(items)
    }

    @Test
    fun verifyLoadNextPageFails() {
        loaderFails = true

        presenterUT.loadNextPage()

        verify(paginable, times(1)).loadMoreFail()
        verify(logger, times(1)).e(error)
    }

    data class MockModel(val s1: String = "s1")
}