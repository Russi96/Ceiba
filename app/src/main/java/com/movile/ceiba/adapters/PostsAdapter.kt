package com.movile.ceiba.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.movile.ceiba.R
import com.movile.ceiba.databinding.PostListItemBinding
import com.movile.ceiba.models.PostItem
import com.movile.ceiba.models.Posts
import com.movile.ceiba.util.UsersDiffUtil

/**
 * Created by danie on 25,septiembre,2021
 */
class PostsAdapter: RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {

    private lateinit var mContext : Context
    private var postList = emptyList<PostItem>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = PostListItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.post_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val posts = postList[position]
        with(holder){
            binding.title.text = posts.title
            binding.body.text = posts.body
        }
    }

    override fun getItemCount() = postList.size

    fun setDataPost(newData: Posts){
        val postsDiffUtil = UsersDiffUtil(postList, newData)
        val diffUtilResult = DiffUtil.calculateDiff(postsDiffUtil)
        postList = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}