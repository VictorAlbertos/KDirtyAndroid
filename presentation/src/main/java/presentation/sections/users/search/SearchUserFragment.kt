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

package presentation.sections.users.search

import com.jakewharton.rxbinding2.view.clicks
import com.squareup.picasso.Picasso
import data.sections.users.User
import io.reactivex.Observable
import kotlinx.android.synthetic.main.user_search_fragment.*
import kotlinx.android.synthetic.main.user_view_group.*
import org.base_app_android.R
import presentation.foundation.views.BaseFragment
import presentation.foundation.views.LayoutResFragment

@LayoutResFragment(R.layout.user_search_fragment)
class SearchUserFragment : BaseFragment<SearchUserPresenter.View, SearchUserPresenter>(), SearchUserPresenter.View {

    override fun initViews() {
        // Do nothing.  Exists to satisfy BaseFragment.
    }

    override fun injectDagger() {
        getApplicationComponent().inject(this)
    }

    override fun clicksSearchUser(): Observable<Unit> {
        return btFindUser.clicks()
    }

    override fun username(): String {
        return etName.text.toString()
    }

    override fun showUser(user: User) {
        user.apply {
            tvName.text = "$id $login"

            if (!avatarUrl.isNullOrEmpty()) {
                Picasso.with(activity).load(avatarUrl)
                        .centerCrop()
                        .fit()
                        .into(ivAvatar)
            }
        }
    }

}
