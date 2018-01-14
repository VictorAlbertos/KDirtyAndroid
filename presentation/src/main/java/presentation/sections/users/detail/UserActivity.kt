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

package presentation.sections.users.detail

import android.view.MenuItem
import com.squareup.picasso.Picasso
import data.sections.users.User
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.user_view_group.*
import org.base_app_android.R
import presentation.foundation.views.BaseActivity
import presentation.foundation.views.LayoutResActivity

@LayoutResActivity(R.layout.user_activity)
class UserActivity : BaseActivity<UserPresenter.View, UserPresenter>(), UserPresenter.View {

    override fun injectDagger() {
        getApplicationComponent().inject(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = getString(R.string.user)
    }

    override fun showUser(user: User) {
        user.apply {
            tvName.text = "$id $login"

            if (!avatarUrl.isNullOrEmpty()) {
                Picasso.with(this@UserActivity).load(avatarUrl)
                        .centerCrop()
                        .fit()
                        .into(ivAvatar)
            }
        }
    }

}
