package appjpm4everyone.baseprojectkotlin

import android.app.Application
import appjpm4everyone.baseprojectkotlin.di.component.AppComponent
import appjpm4everyone.baseprojectkotlin.di.component.DaggerAppComponent
import timber.log.Timber

class BaseProjectKotlinApp : Application() {

    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        component = DaggerAppComponent
            .factory()
            .create(this)
    }
}