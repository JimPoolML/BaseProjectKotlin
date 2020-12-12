package appjpm4everyone.baseprojectkotlin.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import appjpm4everyone.baseprojectkotlin.utils.ScopedViewModel

class MainViewModel() : ScopedViewModel(){

    sealed class UiModel {
        object Loading : UiModel()
        object NavigationLogin : UiModel()
    }

    private val _model = MutableLiveData<UiModel>()
    val modelChooseBusiness: LiveData<UiModel> get() = _model

    fun navigationLogin() {
        _model.value = UiModel.NavigationLogin
    }
}