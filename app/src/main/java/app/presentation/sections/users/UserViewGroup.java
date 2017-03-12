/*
 * Copyright 2016 Victor Albertos
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

package app.presentation.sections.users;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import app.data.sections.users.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import miguelbcr.ok_adapters.recycler_view.OkRecyclerViewAdapter;
import org.base_app_android.R;

public final class UserViewGroup extends FrameLayout implements OkRecyclerViewAdapter.Binder<User> {
  @BindView(R.id.iv_avatar) ImageView ivAvatar;
  @BindView(R.id.tv_name) TextView tvName;

  public UserViewGroup(Context context) {
    super(context);
    init();
  }

  public UserViewGroup(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    View view = LayoutInflater.from(getContext()).inflate(R.layout.user_view_group, this, true);
    ButterKnife.bind(this, view);
  }

  @Override public void bind(User user, int position, int count) {
    tvName.setText(user.getId() + " : " + user.getLogin());

    if (user.getAvatar_url() == null || user.getAvatar_url().isEmpty()) return;

    Picasso.with(getContext()).load(user.getAvatar_url())
        .centerCrop()
        .fit()
        .into(ivAvatar);
  }

}
