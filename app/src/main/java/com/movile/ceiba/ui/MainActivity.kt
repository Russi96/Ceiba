package com.movile.ceiba.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.movile.ceiba.R
import com.movile.ceiba.adapters.UsersAdapter
import com.movile.ceiba.databinding.ActivityMainBinding
import com.movile.ceiba.models.Users
import com.movile.ceiba.models.UsersItem
import com.movile.ceiba.util.NetworkResult
import com.movile.ceiba.util.observeOnce
import com.movile.ceiba.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var mBinding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter by lazy { UsersAdapter() }
    private var users: Users = Users()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupRecyclerView()
        readDatabase()


    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readUsers.observe(this@MainActivity) { database ->
                if (database.isNotEmpty()) {
                    users.clear()
                    database.forEach {
                        val usersItem = UsersItem(
                            id = it.id,
                            name = it.name,
                            email = it.email,
                            phone = it.phone
                        )
                        users.add(usersItem)
                    }
                    mAdapter.setData(users)
                    hideShimmerEffect()
                } else {
                    readAPI()
                }

            }
        }
    }

    private fun readAPI() {
        mainViewModel.getUsers()
        mainViewModel.userResponse.observe(this@MainActivity) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val users = response.data
                    users?.let {
                        hideShimmerEffect()
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        this@MainActivity,
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_users, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.maxWidth = Integer.MAX_VALUE
        searchView?.setOnQueryTextListener(this)
        return true
    }




    private fun setupRecyclerView() {
        mBinding.recyclerViewSearchResults.adapter = mAdapter
        mBinding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(this)
        mBinding.recyclerViewSearchResults.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }
    }


    private fun showShimmerEffect() {
        mBinding.recyclerViewSearchResults.showShimmer()
    }

    private fun hideShimmerEffect() {
        mBinding.recyclerViewSearchResults.hideShimmer()
    }



    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        var searchQuery = query
        searchQuery = "$searchQuery%"
        mainViewModel.searchUsers(searchQuery).observeOnce(this) { database ->
            if (database.isEmpty()) {
                mBinding.emptyList.visibility = View.VISIBLE
                mBinding.emptyListTv.visibility = View.VISIBLE
                mBinding.recyclerViewSearchResults.visibility = View.INVISIBLE
            } else {
                mBinding.emptyList.visibility = View.INVISIBLE
                mBinding.emptyListTv.visibility = View.INVISIBLE
                mBinding.recyclerViewSearchResults.visibility = View.VISIBLE
                mAdapter.searchData(database)
            }

        }
    }
}