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

import com.chad.library.adapter.base.BaseViewHolder
import com.squareup.picasso.Picasso
import data.sections.users.User
import kotlinx.android.synthetic.main.user_view_group.view.*
import org.base_app_android.R
import presentation.foundation.widgets.pager_adapter.PaginationAdapter

class UserAdapter : PaginationAdapter<User, BaseViewHolder>(R.layout.user_view_group) {

    override fun convert(helper: BaseViewHolder, item: User) {
        helper.itemView.apply {
            item.apply {
                tvName.text = "$id $login"

                if (!avatarUrl.isNullOrEmpty()) {
                    Picasso.with(context).load(avatarUrl)
                            .centerCrop()
                            .fit()
                            .into(ivAvatar)
                }
            }
        }
    }
}