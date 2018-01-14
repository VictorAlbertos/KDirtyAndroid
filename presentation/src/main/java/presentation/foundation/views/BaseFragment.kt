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
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import presentation.foundation.BaseApp
import presentation.foundation.dagger.PresentationComponent
import presentation.foundation.presenter.Presenter
import presentation.foundation.presenter.ViewPresenter
import javax.inject.Inject

/**
 * Base class for every new Fragment which requires to use a Presenter. Annotate the sub-class with
 * a {@link LayoutResFragment} annotation to provide a valid LayoutRes identifier.
 *
 * @param <P> the presenter associated with this Fragment.
 */
abstract class BaseFragment<V : ViewPresenter, P : Presenter<V>> : Fragment(), ViewPresenter {
    @Inject internal lateinit var presenter: P

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View? = null

        //Get the value ResLayout from the annotation if provided.
        val layoutResAnnotation = this.javaClass.getAnnotation(LayoutResFragment::class.java)

        if (layoutResAnnotation != null) {
            view = inflater.inflate(layoutResAnnotation.value, container, false)
        }

        //Allows to retain all the dependencies resolved by Dagger between config changes.
        retainInstance = true

        //Try to get the Holder data after config changes by retaining the presenter instance
        // before injecting the graph again and then re asign to the current fragment.
        try {
            //the fuck this crash?
            if (presenter == null) {
            }
            val retained = presenter
            injectDagger()
            presenter = retained
            presenter.onCreatedWasCalled()
        } catch (e: UninitializedPropertyAccessException) {
            injectDagger()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //At this point is safe calling initViews to let the sub-class to configure its views.
        initViews()
        presenter.view = this as V

        //At this point is safe binding the presenter to the lifecycle
        lifecycle.removeObserver(presenter)
        lifecycle.addObserver(presenter)
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
        return (activity!!.application as BaseApp).presentationComponent
    }

    override fun shouldListenForEvents() = !isResumed
}