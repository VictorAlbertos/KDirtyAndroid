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

package app.presentation.sections.users

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import app.data.sections.users.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_view_group.view.*
import miguelbcr.ok_adapters.recycler_view.OkRecyclerViewAdapter
import org.base_app_android.R

class UserViewGroup : FrameLayout, OkRecyclerViewAdapter.Binder<User> {

    constructor(context: Context) : super(context) {
        LayoutInflater.from(context).inflate(R.layout.user_view_group, this, true)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.user_view_group, this, true)
    }

    override fun bind(user: User, position: Int, count: Int) {
        val message = "${user.id.toString()} ${user.login}"
        tvName.text = message

        if (user.avatar_url == null || user.avatar_url.isEmpty()) return

        Picasso.with(context).load(user.avatar_url)
                .centerCrop()
                .fit()
                .into(ivAvatar)
    }
}
