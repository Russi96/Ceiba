package com.movile.ceiba.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.movile.ceiba.R
import com.movile.ceiba.databinding.UserListItemBinding
import com.movile.ceiba.models.Users
import com.movile.ceiba.models.UsersItem
import com.movile.ceiba.ui.PostActivity
import com.movile.ceiba.util.Constants.Companion.USER
import com.movile.ceiba.util.UsersDiffUtil

/**
 * Created by danie on 23,septiembre,2021
 */
class UsersAdapter: RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {

    private lateinit var mContext : Context
    private var userList = emptyList<UsersItem>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = UserListItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = userList[position]
        with(holder){
            binding.name.text = user.name
            binding.phone.text = user.phone
            binding.email.text = user.email
            binding.btnViewPost.setOnClickListener {
                val intent = Intent(mContext, PostActivity::class.java).apply {
                    putExtra(USER, user)
                }
                mContext.startActivity(intent)
            }
        }

    }

    override fun getItemCount() = userList.size

    fun setData(newData: Users){
        diffUsers(newData)
    }

    fun searchData(newData: List<UsersItem>){
        diffUsers(newData)
    }

    private fun diffUsers(newData: List<UsersItem>){
        val usersDiffUtil = UsersDiffUtil(userList, newData)
        val diffUtilResult = DiffUtil.calculateDiff(usersDiffUtil)
        userList = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}