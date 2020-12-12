package appjpm4everyone.baseprojectkotlin.di.component

import android.app.Application
import appjpm4everyone.baseprojectkotlin.di.module.AppModule
import appjpm4everyone.baseprojectkotlin.di.module.SdkModule

import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, SdkModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }

}