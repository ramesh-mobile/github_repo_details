package com.example.githubrepodetailsapp.ui.home.repo_list

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.example.githubrepodetailsapp.R
import com.example.githubrepodetailsapp.data.model.responses.ItemModel
import com.example.githubrepodetailsapp.utils.print

/**
 * Created by ramesh on 20-07-2021
 */
class RepositoryAdapter(val itemList: List<ItemModel?>, val activity: Activity?,val listener: RepoAdapterListener?) :
    RecyclerView.Adapter<RepositoryAdapter.MyViewHolder>() {

    private val TAG = "RepositoryAdapter"
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.imgRepo)
        lateinit var imgRepo : ImageView

        @BindView(R.id.lblTitle)
        lateinit var lblTitle : TextView

        @BindView(R.id.lblDescription)
        lateinit var lblDescription : TextView

        @BindView(R.id.lblLanguage)
        lateinit var lblLanguage : TextView

        @BindView(R.id.lblLicense)
        lateinit var lblLicense : TextView

        init {
            ButterKnife.bind(this,itemView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context) .inflate(R.layout.items, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        activity?.print(TAG,"items:${itemList[position]}")
        var itemModel = itemList[position]
        holder.lblDescription.text = itemModel?.description
        holder.lblTitle.text = itemModel?.name
        holder.lblLanguage.text = itemModel?.language
        holder.lblLicense.text = itemModel?.licenses?.name
        Glide.with(activity!!).load(itemModel?.ownerModel?.avatarUrl).into(holder.imgRepo)
        holder.itemView.setOnClickListener { listener?.onItemClicked(itemModel) }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    interface RepoAdapterListener {
        fun onItemClicked(itemModel: ItemModel?)
    }

}