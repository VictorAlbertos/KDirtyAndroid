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

import android.support.annotation.VisibleForTesting;
import app.data.foundation.Resources;
import app.data.foundation.net.NetworkException;
import io.reactivex.Single;
import io.reactivex.exceptions.CompositeException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import javax.inject.Inject;
import org.base_app_android.BuildConfig;
import org.base_app_android.R;
import timber.log.Timber;

/**
 * Format errors to show the most meaningful message depending on the current build variant and the
 * exception type.
 */
public class ExceptionFormatter {
  private final Resources resources;

  @Inject public ExceptionFormatter(Resources resources, Timber.Tree timberTree) {
    this.resources = resources;
    Timber.plant(timberTree);
  }

  Single<String> format(Throwable throwable) {
    return Single.defer(() -> {
      Timber.e(throwable);

      if (throwable instanceof UnknownHostException
              || throwable instanceof ConnectException) {
        return Single.just(resources.getString(R.string.connection_error));
      }

      if (!isBuildConfigDebug() && !(throwable instanceof NetworkException)) {
        return Single.just(resources.getString(R.string.errors_happen));
      }

      String message = throwable.getMessage();

      if (throwable.getCause() != null && isBuildConfigDebug()) {
        message += System.getProperty("line.separator") + throwable.getCause().getMessage();
      }

      if (throwable instanceof CompositeException) {
        message += System.getProperty("line.separator");
        CompositeException compositeException = (CompositeException) throwable;

        for (Throwable exception : compositeException.getExceptions()) {
          String exceptionName = exception.getClass().getSimpleName();
          String exceptionMessage = exception.getMessage() == null ? "" : exception.getMessage();
          message += exceptionName + " -> " + exceptionMessage + System.getProperty("line.separator");
        }
      }

      return Single.just(message);
    });
  }

  @VisibleForTesting
  boolean isBuildConfigDebug() {
    return BuildConfig.DEBUG;
  }
}
