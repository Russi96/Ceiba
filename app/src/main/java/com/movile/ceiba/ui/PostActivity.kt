package com.movile.ceiba.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import androidx.activity.viewModels

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.movile.ceiba.R
import com.movile.ceiba.adapters.PostsAdapter
import com.movile.ceiba.databinding.ActivityPostBinding
import com.movile.ceiba.models.PostItem
import com.movile.ceiba.models.Posts
import com.movile.ceiba.models.UsersItem
import com.movile.ceiba.util.Constants.Companion.USER
import com.movile.ceiba.util.NetworkResult
import com.movile.ceiba.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostActivity : AppCompatActivity() {


    private lateinit var mBinding: ActivityPostBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter by lazy { PostsAdapter() }
    private var posts: Posts = Posts()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme)
        mBinding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val bundle = intent.extras
        val user = bundle?.getParcelable<UsersItem>(USER) as UsersItem
        with(mBinding) {
            name.text = user.name
            phone.text = user.phone
            email.text = user.email
        }
        setupRecyclerView()
        readDatabasePosts(user.id)

    }

    private fun setupRecyclerView() {
        mBinding.recyclerViewPostsResults.adapter = mAdapter
        mBinding.recyclerViewPostsResults.layoutManager = LinearLayoutManager(this)
        mBinding.recyclerViewPostsResults.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }
    }


    private fun readDatabasePosts(userId: Int) {
        lifecycleScope.launch {
            mainViewModel.readPosts(userId).observe(this@PostActivity) { database ->
                if (database.isNotEmpty()) {
                    posts.clear()
                    database.forEach {
                        val postItem = PostItem(
                            id = it.id,
                            userId = it.userId,
                            title = it.title,
                            body = it.body
                        )
                        posts.add(postItem)
                    }
                    mAdapter.setDataPost(posts)
                    hideShimmerEffect()
                } else {
                    readPostAPI(userId)
                }

            }
        }
    }


    private fun readPostAPI(userId: Int) {
        mainViewModel.getPosts(userId)
        mainViewModel.postsResponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val posts = response.data
                    posts?.let {
                        hideShimmerEffect()
                        mAdapter.setDataPost(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        this,
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

    private fun showShimmerEffect() {
        mBinding.recyclerViewPostsResults.showShimmer()
    }

    private fun hideShimmerEffect() {
        mBinding.recyclerViewPostsResults.hideShimmer()
    }
}