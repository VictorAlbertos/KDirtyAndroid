package presentation.sections.launch

import presentation.foundation.views.BaseActivity

class LaunchActivity : BaseActivity<LaunchPresenter.View, LaunchPresenter>(), LaunchPresenter.View {

    override fun initViews() {
        // Do nothing.  Exists to satisfy BaseActivity.
    }

    override fun injectDagger() {
        getApplicationComponent().inject(this)
    }
}
