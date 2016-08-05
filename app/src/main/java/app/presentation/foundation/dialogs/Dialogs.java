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

package app.presentation.foundation.dialogs;

/*
 * A centralized dialog pipeline. This is indirection layer allows us to run unit tests without mocking the Android platform.
 */
public interface Dialogs {
  /**
   * Show alert dialog with a progress wheel
   */
  void showLoading();

  /**
   * Show alert dialog with a progress wheel which may be canceled by the user.
   */
  void showNoCancelableLoading();

  /**
   * If dialog is shown, hide it.
   */
  void hideLoading();
}
