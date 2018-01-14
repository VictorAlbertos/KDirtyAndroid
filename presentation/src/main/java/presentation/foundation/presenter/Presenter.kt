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

package presentation.foundation.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Base class for every new presenter.

 * @param <V> the associated view abstraction with this presenter.
 */
open class Presenter<V : ViewPresenter> @Inject constructor() : LifecycleObserver {
    protected val disposeOnDestroy = CompositeDisposable()

    protected var onCreatedWasCalled = false

    lateinit var view: V

    fun onCreatedWasCalled() {
        onCreatedWasCalled = true
        disposeOnDestroy.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        disposeOnDestroy.clear()
    }
}
