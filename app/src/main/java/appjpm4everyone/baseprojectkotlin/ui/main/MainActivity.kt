package appjpm4everyone.baseprojectkotlin.ui.main

import android.os.Bundle
import androidx.lifecycle.Observer
import appjpm4everyone.baseprojectkotlin.databinding.ActivityMainBinding
import appjpm4everyone.baseprojectkotlin.ui.base.BaseActivity
import appjpm4everyone.baseprojectkotlin.utils.app
import appjpm4everyone.baseprojectkotlin.utils.custom.CustomProgressBar
import appjpm4everyone.baseprojectkotlin.utils.getViewModel

class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var component: MainActivityComponent
    private val viewModel by lazy {getViewModel{ component.mainViewModel}}
    private val progressBar = CustomProgressBar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //dagger injection
        component = app.component.plus(MainActivityModule())
        initUI()
        viewModel.modelChooseBusiness.observe(this, Observer(::updateUi))
    }

    private fun initUI() {

    }

    private fun updateUi(uiModel: MainViewModel.UiModel) {
        if (uiModel is MainViewModel.UiModel.Loading) progressBar.show(this) else progressBar.hideProgress()
        when (uiModel) {
            is MainViewModel.UiModel.NavigationLogin -> {
                //goToLogin()
            }
        }
    }
}