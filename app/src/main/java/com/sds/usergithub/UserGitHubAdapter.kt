package com.sds.usergithub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_row_user.view.*


class UserGitHubAdapter: RecyclerView.Adapter<UserGitHubAdapter.UserGithubViewHolder>(){
    private var onItemClickCallback: OnItemClickCallback? = null
    private val mData = ArrayList<UserGitHub>()
    fun setData(items: ArrayList<UserGitHub>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class UserGithubViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(userGitHub: UserGitHub){
            with(itemView){
                Glide.with(itemView.context)
                    .load(userGitHub.avatar)
                    .apply(RequestOptions())
                    .into(civ_profile)
                tv_username.text = userGitHub.username
               setOnClickListener { onItemClickCallback?.onItemClicked(userGitHub) }
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserGitHubAdapter.UserGithubViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return UserGithubViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size
    override fun onBindViewHolder(holder: UserGitHubAdapter.UserGithubViewHolder, position: Int) {
        holder.bind(mData[position])
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: UserGitHub)
    }
}