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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.presentation.foundation.BaseApp
import app.presentation.foundation.dagger.PresentationComponent
import app.presentation.foundation.presenter.Presenter
import app.presentation.foundation.presenter.ViewPresenter
import butterknife.ButterKnife
import butterknife.Unbinder
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import com.trello.rxlifecycle2.components.support.RxFragment
import javax.inject.Inject

/**
 * Base class for every new Fragment which requires to use a Presenter. Annotate the sub-class with
 * a {@link LayoutResFragment} annotation to provide a valid LayoutRes identifier.
 *
 * @param <P> the presenter associated with this Fragment.
 */
abstract class BaseFragment<V : ViewPresenter, P : Presenter<V>> : RxFragment(), ViewPresenter {
    @Inject internal lateinit var presenter: P
    private lateinit var unbinder: Unbinder

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View? = null

        //Get the value ResLayout from the annotation if provided.
        val layoutResAnnotation = this.javaClass.getAnnotation(LayoutResFragment::class.java)

        if (layoutResAnnotation != null) {
            view = inflater!!.inflate(layoutResAnnotation.value, container, false)
        }

        //Allows to retain all the dependencies resolved by Dagger between config changes.
        retainInstance = true

        //Prevent injecting the dependency graph again after config changes.
        try {
            //the fuck this crash?
            if (presenter == null) { }
        } catch (e : UninitializedPropertyAccessException) {
            injectDagger()
        }

        //Inject the views with butter-knife.
        unbinder = ButterKnife.bind(this, view!!)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Bind the lifecycle of this Fragment provided by RxLifecycle to the associated presenter.
        presenter.bindLifeCycle(RxLifecycleAndroid.bindFragment<Any>(lifecycle()))

        //At this point is safe calling initViews to let the sub-class to configure its views.
        initViews()

        //At this point is safe calling onBindView from the presenter to provide the data required by the view.
        presenter.onBindView(this as V)
    }

    /**
     * Delegate responsibility to the presenter.
     */
    override fun onResume() {
        super.onResume()
        presenter.onResumeView()
    }

    /**
     * Unbind views injected with Butter-knife.
     */
    override fun onDestroyView() {
        super.onDestroyView()
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
     * the Fragment subclass.
     */
    protected abstract fun injectDagger()

    /**
     * A helper method ro retrieve the PresentationComponent in order to be able to inject the graph
     * in the sub-class.
     */
    protected fun getApplicationComponent(): PresentationComponent {
        return (activity.application as BaseApp)
                .presentationComponent
    }
}