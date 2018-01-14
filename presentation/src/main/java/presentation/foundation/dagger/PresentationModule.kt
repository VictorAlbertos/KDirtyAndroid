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

package presentation.foundation.dagger

import android.app.Application
import android.os.AsyncTask
import dagger.Module
import dagger.Provides
import data.foundation.Resources
import data.foundation.dagger.DataModule
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.base_app_android.BuildConfig
import presentation.foundation.BaseApp
import presentation.foundation.transformations.Transformations
import presentation.foundation.transformations.TransformationsBehaviour
import presentation.foundation.transformations.TransformationsCompletable
import presentation.foundation.transformations.TransformationsCompletableBehaviour
import timber.log.Timber
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = arrayOf(DataModule::class))
class PresentationModule(private val baseApp: BaseApp) {

    @Provides internal fun provideBaseApp(): BaseApp {
        return baseApp
    }

    @Provides internal fun provideApplication(): Application {
        return baseApp
    }

    @Provides internal fun provideTransformations(transformationsBehaviour: TransformationsBehaviour): Transformations {
        return transformationsBehaviour
    }

    @Provides
    internal fun provideTransformationsCompletable(transformationsCompletableBehaviour: TransformationsCompletableBehaviour): TransformationsCompletable {
        return transformationsCompletableBehaviour
    }

    /**
     * Sync with main thread after subscribing to observables emitting from data layer.
     */
    @Named("mainThread")
    @Provides
    internal fun provideSchedulerMainThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    /**
     * Using this executor as the scheduler for all async operations allow us to tell espresso when
     * the app is in an idle state in order to wait for the response.
     */
    @Named("backgroundThread")
    @Provides
    internal fun provideSchedulerBackgroundThread(): Scheduler {
        return Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    /**
     * Resolve a Timber tree which logs important information for crash reporting.
     */
    @Provides
    @Singleton internal fun provideTimberTree(): Timber.Tree {
        return if (BuildConfig.DEBUG) {
            Timber.DebugTree()
        } else {
            object : Timber.Tree() {
                override fun log(priority: Int, tag: String, message: String, t: Throwable) {
                    //FakeCrashLibrary.logError(t);
                }
            }
        }
    }

    @Singleton
    @Provides
    fun provideResources(): Resources {
        return object : Resources {
            override fun getString(idResource: Int) = baseApp.getString(idResource)

            override fun lang() = Locale.getDefault().language
        }
    }
}
