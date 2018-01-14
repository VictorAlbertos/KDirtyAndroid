package presentation.sections.launch

import android.content.Intent
import io.reactivex.Completable
import presentation.foundation.BaseApp
import presentation.sections.dashboard.DashBoardActivity
import javax.inject.Inject

class LaunchWireframe @Inject constructor(private val baseApp: BaseApp) {

    fun dashboard() = Completable.fromAction {
        baseApp.liveActivity.startActivity(Intent(baseApp, DashBoardActivity::class.java))
    }

}