package com.sds.usergithub.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sds.usergithub.DetailActivity
import com.sds.usergithub.FavoriteActivity
import com.sds.usergithub.R
import com.sds.usergithub.UserGitHub
import kotlinx.android.synthetic.main.item_row_fav.view.*
import java.util.ArrayList

class FavoriteAdapter(private val activity: Activity): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    var mData = ArrayList<UserGitHub>()
    set(mData) {
        if (mData.size > 0){
            this.mData.clear()
        }
        this.mData.addAll(mData)
        notifyDataSetChanged()
    }
    fun addItem(userGitHub: UserGitHub){
        this.mData.add(userGitHub)
        notifyItemInserted(this.mData.size-1)
    }
    fun removeItem(position: Int){
        this.mData.removeAt(position)
        notifyItemChanged(position)
        notifyItemRangeChanged(position, this.mData.size)
    }

    inner class FavoriteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(userGitHub: UserGitHub){
            with(itemView){
                Glide.with(itemView.context)
                    .load(userGitHub.avatar)
                    .apply(RequestOptions())
                    .into(civ_profileFav)
                tv_usernameFav.text = userGitHub.username
                setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback{
                    override fun onItemClicked(view: View, position: Int) {
                        //di isi sesuai API

                    }

                }))
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

}
class CustomOnItemClickListener(private val position: Int, private val onItemClickCallback: OnItemClickCallback): View.OnClickListener{
    override fun onClick(p0: View?) {
        onItemClickCallback.onItemClicked(p0!!, position)
    }
    interface OnItemClickCallback {
        fun onItemClicked(view: View, position: Int)
    }
}