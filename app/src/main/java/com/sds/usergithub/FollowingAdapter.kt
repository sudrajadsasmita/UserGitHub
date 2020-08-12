package com.sds.usergithub

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row_following.view.*

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null
    private val mData = ArrayList<UserGitHub>()
    fun setOnItemClickedCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    fun setData(items: ArrayList<UserGitHub>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }
    inner class FollowingViewHolder(itemVIew: View): RecyclerView.ViewHolder(itemVIew) {
        fun bind(userGitHub: UserGitHub){
            with(itemView){
                tv_username_following.text = userGitHub.username
                Glide.with(itemView.context)
                    .load(userGitHub.avatar)
                    .apply(RequestOptions())
                    .into(civ_profile_following)
                setOnClickListener { onItemClickCallback?.onItemClicked(userGitHub) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowingAdapter.FollowingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_following, parent, false)
        return FollowingViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: FollowingAdapter.FollowingViewHolder, position: Int) {
        return holder.bind(mData[position])
    }
    interface OnItemClickCallback{
        fun onItemClicked(data: UserGitHub)
    }
}