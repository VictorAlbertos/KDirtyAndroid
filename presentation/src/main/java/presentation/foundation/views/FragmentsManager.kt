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

package presentation.foundation.views

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import javax.inject.Inject

/**
 * Support class to work with fragments
 */
class FragmentsManager @Inject constructor() {

    /**
     * Gets the current fragment for the given `idFrameLayout`
     * @param fragmentManager A [FragmentManager] object
     * @param idFrameLayout A [android.widget.FrameLayout] id
     * @return The current fragment loaded on `idFrameLayout`
     */
    fun getCurrentFragment(fragmentManager: FragmentManager, @IdRes idFrameLayout: Int): Fragment? {
        return fragmentManager.findFragmentById(idFrameLayout)
    }

    /**
     * Replace the `classFragment` into the `idFrameLayout`
     * @param fragmentManager A [FragmentManager] object
     * @param idFrameLayout A [android.widget.FrameLayout] id
     * @param classFragment A [Fragment] class
     * @param forceReplacement true to force replacement, false to perform the replacement if the
     * current fragment is not currently loaded on `idFrameLayout`
     * @return true if the replacement was performed, false otherwise
     */
    @JvmOverloads fun replaceFragment(fragmentManager: FragmentManager,
                                      @IdRes idFrameLayout: Int,
                                      classFragment: Class<out Fragment>,
                                      forceReplacement: Boolean = false): Boolean {
        val fragment = classFragment.newInstance()
        val current = getCurrentFragment(fragmentManager, idFrameLayout)

        val replace = forceReplacement
                || current == null
                || current.javaClass != classFragment

        if (replace) {
            fragmentManager.beginTransaction()
                    .replace(idFrameLayout, fragment)
                    .commit()
        }

        return replace
    }
}
