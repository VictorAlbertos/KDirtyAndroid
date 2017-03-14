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

package app.presentation.sections.users.list

import android.support.v7.widget.GridLayoutManager
import android.view.ViewGroup
import app.data.sections.users.User
import app.presentation.foundation.views.BaseFragment
import app.presentation.foundation.views.LayoutResFragment
import app.presentation.sections.users.UserViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.users_fragment.*
import miguelbcr.ok_adapters.recycler_view.OkRecyclerViewAdapter
import miguelbcr.ok_adapters.recycler_view.Pager
import org.base_app_android.R

@LayoutResFragment(R.layout.users_fragment)
class UsersFragment : BaseFragment<UsersPresenter.View, UsersPresenter>(), UsersPresenter.View {
    private lateinit var adapter: OkRecyclerViewAdapter<User, UserViewGroup>
    private var positionRecyclerState: Int = 0

    override fun injectDagger() {
        getApplicationComponent().inject(this)
    }

    override fun initViews() {
        adapter = object : OkRecyclerViewAdapter<User, UserViewGroup>() {
            override fun onCreateItemView(parent: ViewGroup, viewType: Int): UserViewGroup {
                return UserViewGroup(activity)
            }
        }

        val layoutManager = GridLayoutManager(activity, 2)
        adapter.configureGridLayoutManagerForPagination(layoutManager)

        rvUsers.layoutManager = layoutManager
        rvUsers.adapter = adapter

        layoutManager.scrollToPosition(positionRecyclerState)
    }

    override fun setUpLoaderPager(initialLoad: List<User>,
                                  loaderPager: Pager.LoaderPager<User>) {
        adapter.setPager(R.layout.srv_progress, initialLoad, loaderPager)
    }

    override fun setUpRefreshList(call: Pager.Call<User>) {
        swipeRefreshUsers.setOnRefreshListener { adapter.resetPager(call) }
    }

    override fun userSelectedClicks(): Observable<User> {
        val clicks = PublishSubject.create<User>()
        adapter.setOnItemClickListener { user, _, _ -> clicks.onNext(user) }
        return clicks
    }

    override fun hideLoadingOnRefreshList() {
        swipeRefreshUsers.isRefreshing = false
    }

    override fun onDestroyView() {
        positionRecyclerState = (rvUsers.layoutManager as GridLayoutManager)
                .findFirstCompletelyVisibleItemPosition()
        super.onDestroyView()
    }

}
