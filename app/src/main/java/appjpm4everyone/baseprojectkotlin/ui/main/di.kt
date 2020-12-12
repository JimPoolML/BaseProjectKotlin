package appjpm4everyone.baseprojectkotlin.ui.main

import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class MainActivityModule {

    @Provides
    fun MainViewModelProvider(): MainViewModel{
        return MainViewModel(
        )
    }
}

@Subcomponent(modules = [(MainActivityModule::class)])
interface MainActivityComponent {
    val mainViewModel: MainViewModel
}