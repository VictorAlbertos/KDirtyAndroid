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

package presentation.foundation

import android.app.Activity
import android.app.Application
import com.akaita.java.rxjava2debug.RxJava2Debug
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.leakcanary.LeakCanary
import data.foundation.dagger.DataModule
import presentation.foundation.dagger.DaggerPresentationComponent
import presentation.foundation.dagger.PresentationComponent
import presentation.foundation.dagger.PresentationModule
import presentation.foundation.widgets.Notifications




/**
 * Custom Application
 */
class BaseApp : Application() {
    /**
     * Expose the PresentationComponent single instance in order to inject on demand the dependency
     * graph in both Fragments and Activities.
     */
    lateinit var presentationComponent: PresentationComponent
        private set

    /**
     * Expose a reference to current Activity to be used for other classes which may depend on it.
     *
     * @see Notifications as an example.
     */
    val liveActivity: Activity get() = AppCare.getLiveActivity()!!

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }

        LeakCanary.install(this)
        AppCare.registerActivityLifeCycle(this)
        initDaggerComponent()

        // Enable RxJava assembly stack collection, to make RxJava crash reports clear and unique
        // Make sure this is called AFTER setting up any Crash reporting mechanism as Crashlytics
        RxJava2Debug.enableRxJava2AssemblyTracking(arrayOf("org.base.app"))
    }

    private fun initDaggerComponent() {
        val presentationModule = PresentationModule(this)

        initStetho()

        presentationComponent = DaggerPresentationComponent.builder()
                .presentationModule(PresentationModule(this))
                .dataModule(DataModule(this.filesDir, StethoInterceptor()))
                .build()
    }

    private fun initStetho() {
        val initializerBuilder = Stetho.newInitializerBuilder(this)
        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
        Stetho.initialize(initializerBuilder.build())
    }
}
