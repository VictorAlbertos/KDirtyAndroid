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

package app.presentation.foundation.rx_bindings_interop;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;
import app.data.foundation.Ignore;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Observable;

/**
 * Workaround to convert between RxJava 1.x and 2.x reactive types for RxBindings.
 */
public final class RxView {

  @CheckResult @NonNull
  public static Observable<Ignore> clicks(@NonNull View view) {
    return RxJavaInterop.toV2Observable(
        com.jakewharton.rxbinding.view.RxView.clicks(view).map(null_ -> Ignore.Get)
    );
  }
}
