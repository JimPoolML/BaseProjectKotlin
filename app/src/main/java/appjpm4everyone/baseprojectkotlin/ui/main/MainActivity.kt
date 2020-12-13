package appjpm4everyone.baseprojectkotlin.ui.main

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import appjpm4everyone.baseprojectkotlin.R
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
    private lateinit var dogsAdapter: DogsAdapter

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
        binding.searchBreed.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboardFrom(this@MainActivity)
                viewModel.searchDogByName(query.toLowerCase())
                return true
            }

        })
    }

    private fun updateUi(uiModel: MainViewModel.UiModel) {
        if (uiModel is MainViewModel.UiModel.Loading) progressBar.show(this) else progressBar.hideProgress()
        when (uiModel) {
            is MainViewModel.UiModel.ShowDogsError ->  showDogsResponseError(uiModel.errorStatus)
            is MainViewModel.UiModel.ShowEmptyList ->  showDogsResponseError(getString(R.string.empty_list))
            is MainViewModel.UiModel.ShowDogList ->  showDogsSuccess(uiModel.dogsList)
        }
    }

    private fun showDogsSuccess(dogsList: List<String>) {
        dogsAdapter = DogsAdapter(dogsList)
        binding.rvDogs.setHasFixedSize(true)
        binding.rvDogs.layoutManager = LinearLayoutManager(this)
        binding.rvDogs.adapter = dogsAdapter
        val dividerItemDecoration = DividerItemDecoration( this, LinearLayoutManager.HORIZONTAL)
        binding.rvDogs.addItemDecoration(dividerItemDecoration)
        hideKeyboardFrom(this)
    }

    private fun showDogsResponseError(errorStatus: String) {
        hideKeyboardFrom(this)
        showShortSnackError(this, errorStatus)
    }
}