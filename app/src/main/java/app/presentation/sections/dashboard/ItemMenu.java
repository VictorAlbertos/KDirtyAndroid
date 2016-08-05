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

package app.presentation.sections.dashboard;

import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import com.google.auto.value.AutoValue;

@AutoValue
abstract class ItemMenu {
  public abstract Class<? extends Fragment> classFragment();
  @StringRes public abstract int resTitle();

  public static ItemMenu create(Class<? extends Fragment> classFragment, int resTitle) {
    return new AutoValue_ItemMenu(classFragment, resTitle);
  }
}
