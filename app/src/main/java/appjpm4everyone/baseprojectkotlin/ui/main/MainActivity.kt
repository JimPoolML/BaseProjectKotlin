package appjpm4everyone.baseprojectkotlin.ui.main

import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import androidx.cursoradapter.widget.SimpleCursorAdapter
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
    //To RecyclerView
    private lateinit var dogsAdapter: DogsAdapter

    private lateinit var list : List<String>
    private lateinit var mAdapter : SimpleCursorAdapter


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
        binding.searchBreed.clearFocus()

        list = resources.getStringArray(R.array.dog_list).toList()

        val from = arrayOf("dogsFound")
        val to = intArrayOf(android.R.id.text1)
        mAdapter = SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)



        binding.searchBreed.suggestionsAdapter = mAdapter


        binding.searchBreed.setOnSuggestionListener(object:
            androidx.appcompat.widget.SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = mAdapter.getItem(position) as Cursor
                val txt = cursor.getString(cursor.getColumnIndex("dogsFound"))
                binding.searchBreed.setQuery(txt, false)
                viewModel.searchDogByName(txt.toLowerCase())
                hideKeyboardFrom(this@MainActivity)
                return true
            }
        })

        binding.searchBreed.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                populateAdapter(newText);
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if (list.contains(query)) {
                    viewModel.searchDogByName(query.toLowerCase())
                } else {
                    showDogsResponseError(getString(R.string.no_found))
                }
                hideKeyboardFrom(this@MainActivity)
                return false
            }

        })
    }

    // You must implements your logic to get data using OrmLite
    private fun populateAdapter(query: String) {
        val c = MatrixCursor(arrayOf(BaseColumns._ID, "dogsFound"))
        for (i in list.indices) {
            if (list[i].toLowerCase()
                    .startsWith(query.toLowerCase())
            ) c.addRow(arrayOf<Any>(i, list[i]))
        }
        mAdapter.changeCursor(c)
    }

    private fun updateUi(uiModel: MainViewModel.UiModel) {
        if (uiModel is MainViewModel.UiModel.Loading) progressBar.show(this) else progressBar.hideProgress()
        when (uiModel) {
            is MainViewModel.UiModel.ShowDogsError ->  showDogsResponseError(uiModel.errorStatus)
            is MainViewModel.UiModel.ShowEmptyList ->  showDogsResponseError(getString(R.string.empty_list))
            is MainViewModel.UiModel.ShowDogList ->  showDogsSuccess(uiModel.dogsList)
        }
        hideKeyboardFrom(this)
    }

    //To easily test
    private fun getArrayString(): Array<String?>? {
        return applicationContext.resources.getStringArray(R.array.dog_list)
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