package com.example.githubrepodetailsapp.ui.home.repo_details

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.example.githubrepodetailsapp.R
import com.example.githubrepodetailsapp.data.model.responses.ItemModel
import com.example.githubrepodetailsapp.utils.Constants

/**
 * Created by ramesh on 20-07-2021
 */
class RepoDetailsActivity : AppCompatActivity() {
    private val TAG = "RepoDetailsActivity"

    @BindView(R.id.imgViewProfilePic)
    lateinit var imgViewProfilePic : ImageView

    @BindView(R.id.lblOwnerName)
    lateinit var lblOwnerName : TextView

    @BindView(R.id.lblProjectHeading)
    lateinit var lblProjectHeading : TextView

    @BindView(R.id.lblDescription)
    lateinit var lblDescription : TextView

    @BindView(R.id.lblLanguage)
    lateinit var lblLanguage : TextView

     @BindView(R.id.lblLicense)
     lateinit var lblLicense : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_details)
        ButterKnife.bind(this)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        var itemModel : ItemModel? = intent?.getSerializableExtra(Constants.ITEM_DETAILS) as ItemModel?
        Glide.with(this).load(itemModel?.ownerModel?.avatarUrl).into(imgViewProfilePic!!)
        lblProjectHeading?.setText("Name:${(itemModel?.name)?:"Not Avalable"}")
        lblOwnerName?.setText("Owner:${(itemModel?.ownerModel?.name)?:"Not Avalable"}")
        lblDescription.setText("Desc: ${(itemModel?.description)?:"Not Avalable"}")
        lblLanguage?.setText("Language: ${(itemModel?.language)?:"Not Avalable"}")
        lblLicense?.setText("License: ${(itemModel?.licenses?.name)?:"Not Avalable"}")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        onBackPressed()
        return false

    }
}