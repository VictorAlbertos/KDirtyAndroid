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

import android.support.v7.widget.GridLayoutManager
import data.sections.users.User
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.users_fragment.*
import org.base_app_android.R
import presentation.foundation.views.BaseFragment
import presentation.foundation.views.LayoutResFragment

@LayoutResFragment(R.layout.users_fragment)
class UsersFragment : BaseFragment<UsersPresenter.View, UsersPresenter>(), UsersPresenter.View {
    override val paginable by lazy { UserAdapter() }

    override val onRefreshSignals = PublishSubject.create<Unit>()

    override fun injectDagger() {
        getApplicationComponent().inject(this)
    }

    override fun initViews() {
        swipeRefreshUsers.setOnRefreshListener { onRefreshSignals.onNext(Unit) }

        rvUsers.layoutManager = GridLayoutManager(activity, 2)
        rvUsers.adapter = paginable
    }

    override fun userSelectedClicks(): Observable<User> {
        val clicks = PublishSubject.create<User>()
        paginable.setOnItemClickListener { _, _, position ->
            clicks.onNext(paginable.data[position])
        }
        return clicks
    }

    override fun hideLoading() {
        swipeRefreshUsers.isRefreshing = false
    }

    override fun showLoading() {
        swipeRefreshUsers.isRefreshing = true
    }
}
