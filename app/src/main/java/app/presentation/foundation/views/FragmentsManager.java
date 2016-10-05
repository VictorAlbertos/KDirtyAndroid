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

package app.presentation.foundation.views;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

/**
 * Support class to work with fragments
 */
public class FragmentsManager {

  @Inject public FragmentsManager() {
  }

  /**
   * Gets the current fragment for the given {@code idFrameLayout}
   *
   * @param fragmentManager A {@link FragmentManager} object
   * @param idFrameLayout A {@link android.widget.FrameLayout} id
   * @return The current fragment loaded on {@code idFrameLayout}
   */
  public Fragment getCurrentFragment(FragmentManager fragmentManager, @IdRes int idFrameLayout) {
    return fragmentManager.findFragmentById(idFrameLayout);
  }

  /**
   * Replace the {@code classFragment} into the {@code idFrameLayout}
   *
   * @param fragmentManager A {@link FragmentManager} object
   * @param idFrameLayout A {@link android.widget.FrameLayout} id
   * @param classFragment A {@link Fragment} class
   * @return true if the replacement was performed, false otherwise
   */
  public boolean replaceFragment(FragmentManager fragmentManager,
                                 @IdRes int idFrameLayout,
                                 Class<? extends Fragment> classFragment) {
    return replaceFragment(fragmentManager, idFrameLayout, classFragment, false);
  }

  /**
   * Replace the {@code classFragment} into the {@code idFrameLayout}
   *
   * @param fragmentManager A {@link FragmentManager} object
   * @param idFrameLayout A {@link android.widget.FrameLayout} id
   * @param classFragment A {@link Fragment} class
   * @param forceReplacement true to force replacement, false to perform the replacement if the
   *                         current fragment is not currently loaded on {@code idFrameLayout}
   * @return true if the replacement was performed, false otherwise
   */
  public boolean replaceFragment(FragmentManager fragmentManager,
                                 @IdRes int idFrameLayout,
                                 Class<? extends Fragment> classFragment,
                                 boolean forceReplacement) {
    try {
      Fragment fragment = classFragment.newInstance();
      Fragment current = getCurrentFragment(fragmentManager, idFrameLayout);

      boolean replace = forceReplacement
              || current == null
              || current.getClass() != classFragment;

      if (replace) {
        fragmentManager.beginTransaction()
                .replace(idFrameLayout, fragment)
                .commit();
      }

      return replace;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
