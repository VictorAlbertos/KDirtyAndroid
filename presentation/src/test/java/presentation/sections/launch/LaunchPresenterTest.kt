package presentation.sections.launch

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import org.junit.Test

class LaunchPresenterTest {
    private val view = mock<LaunchPresenter.View>()
    private val launchWireframe = mock<LaunchWireframe>()

    private val presenterUT by lazy {
        val presenter = LaunchPresenter(launchWireframe)
        presenter.view = view
        presenter
    }

    @Test
    fun verify_OnCreate() {
        whenever(launchWireframe.dashboard()).thenReturn(Completable.complete())

        presenterUT.onCreate()

        verify(launchWireframe).dashboard()
    }
}