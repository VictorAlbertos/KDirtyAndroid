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

package app.presentation.foundation.views

import android.os.Bundle
import app.presentation.foundation.BaseApp
import app.presentation.foundation.dagger.PresentationComponent
import app.presentation.foundation.presenter.Presenter
import app.presentation.foundation.presenter.ViewPresenter
import butterknife.ButterKnife
import butterknife.Unbinder
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import javax.inject.Inject

/**
 * Base class for every new Activity which requires to use a Presenter. Annotate the sub-class with
 * a {@link LayoutResActivity} annotation to provide a valid LayoutRes identifier.
 *
 * @param <P> the presenter associated with this Activity.
 */
abstract class BaseActivity<V : ViewPresenter, P : Presenter<V>> : RxAppCompatActivity(), ViewPresenter {
    @Inject lateinit internal var presenter: P
    private lateinit var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Get the value ResLayout from the annotation if provided.
        val layoutRes : LayoutResActivity? = this.javaClass.getAnnotation(LayoutResActivity::class.java)
        if (layoutRes != null) setContentView(layoutRes?.value)

        //Inject the views with butter-knife.
        unbinder = ButterKnife.bind(this)

        //Try to get the Holder data if the Activity has been destroyed due to config changes in order to prevent
        // injecting the dependency graph again.
        if (lastCustomNonConfigurationInstance == null) {
            injectDagger()
        } else {
            presenter = lastCustomNonConfigurationInstance as P
        }

        //Bind the lifecycle of this Activity provided by RxLifecycle to the associated presenter.
        presenter.bindLifeCycle(RxLifecycleAndroid.bindActivity<Any>(lifecycle()))

        //At this point is safe calling initViews to let the sub-class to configure its views.
        initViews()

        //At this point is safe calling onBindView from the presenter to provide the data required by the view.
        presenter.onBindView(this as V)
    }

    /**
     * Delegate responsibility to the presenter.
     */
    public override fun onResume() {
        super.onResume()
        presenter.onResumeView()
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
     * Unbind views injected with Butter-knife.
     */
    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
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
     * A helper method ro retrieve the PresentationComponent in order to be able to inject the graph
     * in the sub-class.
     */
    protected fun getApplicationComponent(): PresentationComponent {
        return (application as BaseApp).presentationComponent
    }
}
