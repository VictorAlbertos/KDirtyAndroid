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

package presentation.sections.users.list

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import data.foundation.extentions.addTo
import data.sections.users.User
import data.sections.users.UserRepository
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import presentation.foundation.presenter.Presenter
import presentation.foundation.presenter.ViewPresenter
import presentation.foundation.transformations.Transformations
import presentation.foundation.widgets.pager_adapter.Paginable
import presentation.foundation.widgets.pager_adapter.PaginationAdapterPresenter
import presentation.sections.users.UsersWireframe
import javax.inject.Inject

class UsersPresenter @Inject constructor(private val repository: UserRepository,
                                         private val wireframe: UsersWireframe,
                                         private val transformations: Transformations,
                                         private val pager: PaginationAdapterPresenter<User>) : Presenter<UsersPresenter.View>() {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        startPaging(false)

        view.onRefreshSignals.subscribe {
            pager.resetCache()
            pager.resetPagination()
            startPaging(true)
        }.addTo(disposeOnDestroy)

        view.userSelectedClicks()
                .flatMapCompletable { user -> wireframe.userScreen(user) }
                .subscribe().addTo(disposeOnDestroy)
    }

    private fun startPaging(refresh: Boolean) {
        view.apply {
            showLoading()

            val loader = { page: Int? ->
                repository.getUsers(page, refresh && page == null)
                        .compose(transformations.schedules())
                        .compose(transformations.reportOnSnackBar())
            }

            pager.init(loader, paginable, disposeOnDestroy, 20, Consumer {
                hideLoading()
            })

            pager.loadNextPage()
        }
    }

    interface View : ViewPresenter {
        val paginable: Paginable<User>
        val onRefreshSignals: Observable<Unit>

        fun showLoading()
        fun hideLoading()
        fun userSelectedClicks(): Observable<User>
    }
}
