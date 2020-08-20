package com.sds.usergithub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sds.usergithub.R
import com.sds.usergithub.UserGitHub
import kotlinx.android.synthetic.main.item_row_followers.view.*
import java.util.ArrayList

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val mData = ArrayList<UserGitHub>()

    fun setData(items: ArrayList<UserGitHub>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }
    private var onItemClickCallback: OnItemClickCallback? = null
    private fun setOnItemCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback=onItemClickCallback
    }
    inner class FavoriteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(userGitHub: UserGitHub){
            with(itemView){
                Glide.with(itemView.context)
                    .load(userGitHub.avatar)
                    .apply(RequestOptions())
                    .into(civ_profile_followers)
                tv_username_followers.text = userGitHub.username
                setOnClickListener { onItemClickCallback?.onItemClicked(userGitHub) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteAdapter.FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_fav,parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        holder.bind(mData[position])
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: UserGitHub)
    }
}