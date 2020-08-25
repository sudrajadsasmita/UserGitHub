package com.sds.usergithub.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.sds.usergithub.DetailActivity
import com.sds.usergithub.R
import com.sds.usergithub.UserGitHub
import com.sds.usergithub.adapter.FollowersAdapter
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_followers.*
import org.json.JSONArray
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowersFragment : Fragment() {
    private val list = ArrayList<UserGitHub>()
    private lateinit var followersAdapter: FollowersAdapter
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val userGitHub = activity?.intent?.getParcelableExtra<UserGitHub>(
            DetailActivity.EXTRA_USER
        )!!
        followersAdapter = FollowersAdapter()
        val url = "https://api.github.com/users/${userGitHub.username}/followers"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token a430310dec032673be6e899c91309aa8ae6bb3de")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()){
                        val items = responseArray.getJSONObject(i)
                        val username = items.getString("login")
                        val profile = items.getString("avatar_url")
                        list.add(
                            UserGitHub(
                                null,
                                username,
                                null,
                                profile,
                                null,
                                null,
                                null,
                                null,
                                null
                            )
                        )
                    }
                    showLoading(false)
                    rv_followers.layoutManager  = LinearLayoutManager(activity)
                    rv_followers.adapter = followersAdapter
                    followersAdapter.setData(list)
                    followersAdapter.setOnItemClickCallback(object : FollowersAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: UserGitHub) {
                            val intent = Intent(activity, DetailActivity::class.java)
                            intent.putExtra(DetailActivity.EXTRA_USER, data)
                            startActivity(intent)
                        }
                    })
                }catch (e: Exception){
                    Log.d("onResponse:", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.e("onError", error.toString())
            }
        })
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FollowersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FollowersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun showLoading(state: Boolean) {
        if (state) {
            progressBar_followers.visibility = View.VISIBLE
        } else {
            progressBar_followers.visibility = View.GONE
        }
    }
}