package presentation.sections.launch

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import data.foundation.extentions.addTo
import presentation.foundation.presenter.Presenter
import presentation.foundation.presenter.ViewPresenter
import javax.inject.Inject

class LaunchPresenter @Inject constructor(private val launchWireframe: LaunchWireframe) : Presenter<LaunchPresenter.View>() {
    interface View : ViewPresenter

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        launchWireframe.dashboard().subscribe().addTo(disposeOnDestroy)
    }
}