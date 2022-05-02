package com.movile.ceiba.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*

import com.movile.ceiba.data.Repository
import com.movile.ceiba.data.database.entities.PostsEntity
import com.movile.ceiba.data.database.entities.UsersEntity
import com.movile.ceiba.models.Posts
import com.movile.ceiba.models.Users
import com.movile.ceiba.models.UsersItem
import com.movile.ceiba.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** Room Database */

    val readUsers: LiveData<List<UsersEntity>> = repository.localDataSource.readUsers().asLiveData()


    fun readPosts(userId: Int): LiveData<List<PostsEntity>>{
        return repository.localDataSource.readPosts(userId).asLiveData()
    }


    private fun insertUsers(usersEntity: UsersEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.localDataSource.insertUsers(usersEntity)
    }

    private fun insertPosts(postsEntity: PostsEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.localDataSource.insertPosts(postsEntity)
    }

    fun searchUsers(searchQuery: String): LiveData<List<UsersItem>> {
        return repository.localDataSource.searchUsers(searchQuery).asLiveData()
    }


    /** Retrofit */

    var userResponse: MutableLiveData<NetworkResult<Users>> = MutableLiveData()

    var postsResponse: MutableLiveData<NetworkResult<Posts>> = MutableLiveData()


    fun getUsers() = viewModelScope.launch {
        getUsersSafeCall()
    }

    fun getPosts(userId: Int) = viewModelScope.launch {
        getPostsSafeCall(userId)
    }

    private suspend fun getPostsSafeCall(userId: Int) {
        postsResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val responsePost = repository.remoteDataSource.getAllPost(userId)
                postsResponse.value = handlePostsResponse(responsePost)
                val posts = postsResponse.value?.data
                posts?.forEach {
                    cachePosts(it.id, it.userId, it.title, it.body)
                }
            } catch (e: Exception) {
                userResponse.value = NetworkResult.Error("No Post Found!!")
            }
        } else {
            userResponse.value = NetworkResult.Error("No Internet Connection!!")
        }
    }

    private suspend fun getUsersSafeCall() {
        userResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remoteDataSource.getAllUsers()
                userResponse.value = handleUsersResponse(response)
                val users = userResponse.value?.data
                users?.forEach {
                    cacheUsers(it.id, it.name, it.email, it.phone)
                }
            } catch (e: Exception) {

                userResponse.value = NetworkResult.Error("No Users Found!!")
            }
        } else {
            userResponse.value = NetworkResult.Error("No Internet Connection!!")
        }
    }

    private fun cachePosts(id: Int, userId: Int, title: String, body : String){
        val postsEntity = PostsEntity(id, userId, title, body)
        insertPosts(postsEntity)
    }

    private fun cacheUsers(id: Int, name: String, email: String, phone: String) {
        val usersEntity = UsersEntity(id, name, email, phone)
        insertUsers(usersEntity)
    }

    private fun handlePostsResponse(response: Response<Posts>): NetworkResult<Posts>? {
        return when {
            response.isSuccessful -> {
                val post = response.body()
                post?.let {
                    return NetworkResult.Success(post)
                }
            }
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.body().isNullOrEmpty() -> {
                return NetworkResult.Error("User not found.")
            }

            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleUsersResponse(response: Response<Users>): NetworkResult<Users>? {

        return when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }

            response.body().isNullOrEmpty() -> {
                return NetworkResult.Error("User not found.")
            }
            response.isSuccessful -> {
                val users = response.body()

                users?.let {
                    return NetworkResult.Success(it)
                }
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}