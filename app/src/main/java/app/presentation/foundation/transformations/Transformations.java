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

package app.presentation.foundation.transformations;

import rx.Observable;

public interface Transformations {

  /**
   * Set the associated lifecycle to RxLifecycle be able to handle the observable subscriptions, for
   * both Fragments and Activities.
   */
  void setLifecycle(Observable.Transformer lifecycle);

  /**
   * Bind the subscription observable to the {@link Transformations#setLifecycle} supplied. Prepare
   * the observable to use an io thread for subscription and to observe on the UI thread only after
   * the stream of data has reached this point.
   */
  <T> Observable.Transformer<T, T> safely();

  /**
   * Outputs the formatted exception on {@link android.support.design.widget.Snackbar} and resume
   * the error returning an empty observable.
   */
  <T> Observable.Transformer<T, T> reportOnSnackBar();

  /**
   * Outputs the formatted exception on {@link android.widget.Toast} and resume the error returning
   * an empty observable.
   */
  <T> Observable.Transformer<T, T> reportOnToast();

  /**
   * Show a loading dialog just before observables is subscribed and hide it after it is completed.
   */
  <T> Observable.Transformer<T, T> loading();
}
