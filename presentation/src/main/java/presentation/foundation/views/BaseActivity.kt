/*
 * Copyright 2017 Victor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package presentation.foundation.views

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import presentation.foundation.BaseApp
import presentation.foundation.dagger.PresentationComponent
import presentation.foundation.presenter.Presenter
import presentation.foundation.presenter.ViewPresenter
import javax.inject.Inject

/**
 * Base class for every new Activity which requires to use a Presenter. Annotate the sub-class with
 * a {@link LayoutResActivity} annotation to provide a valid LayoutRes identifier.
 *
 * @param <P> the presenter associated with this Activity.
 */
abstract class BaseActivity<V : ViewPresenter, P : Presenter<V>> : AppCompatActivity(), ViewPresenter {
    @Inject lateinit var presenter: P
    private var isResumed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Get the value ResLayout from the annotation if provided.
        val layoutRes: LayoutResActivity? = this.javaClass.getAnnotation(LayoutResActivity::class.java)
        if (layoutRes != null) setContentView(layoutRes?.value)

        //Try to get the Holder data if the Activity has been destroyed due to config changes by retaining the presenter instance
        // before injecting the graph again and then re asign to the current activity.
        if (lastCustomNonConfigurationInstance == null) {
            injectDagger()
        } else {
            val retained = lastCustomNonConfigurationInstance as P
            injectDagger()
            presenter = retained
            presenter.onCreatedWasCalled()
        }

        //At this point is safe calling initViews to let the sub-class to configure its views.
        initViews()
        presenter.view = this as V

        //At this point is safe binding the presenter to the lifecycle
        lifecycle.removeObserver(presenter)
        lifecycle.addObserver(presenter)
    }

    override fun onResume() {
        super.onResume()
        isResumed = true
    }

    override fun onPause() {
        super.onPause()
        isResumed = false
    }

    /**
     * By calling this method and returning the presenter we make sure that this instance will be
     * retain between config changes, that way its state will be preserve. Every data variable not
     * managed by ReactiveCache which needs to survive configs changes must be declared in the
     * presenter nor in the activity, otherwise it won't survive config changes.
     */
    override fun onRetainCustomNonConfigurationInstance(): Any {
        return presenter
    }

    /**
     * Implement this method to access any reference view because they all are properly initialized at
     * this point.
     */
    protected abstract fun initViews()

    /**
     * Force to the sub-class to implement this method as a reminder that the dependency graph built
     * by Dagger requires to be injected using getApplicationComponent().inject(this), where 'this' is
     * the Activity subclass.
     */
    protected abstract fun injectDagger()

    /**
     * A helper method to retrieve the PresentationComponent in order to be able to inject the graph
     * in the sub-class.
     */
    protected fun getApplicationComponent(): PresentationComponent {
        return (application as BaseApp).presentationComponent
    }

    override fun shouldListenForEvents() = !isResumed
}
