package com.honeywell.assignment.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.honeywell.assignment.R
import com.honeywell.assignment.databinding.ActivityArticlesDetailsBinding
import com.honeywell.assignment.model.Hit


class ArticlesDetails:AppCompatActivity() {

    lateinit var binding:ActivityArticlesDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_articles_details)
        intent?.let {
            val gson = Gson()
            val hit = gson.fromJson<Hit>(
                intent.getStringExtra(ArticlesActivity.SEND_HIT_OBJECT),
                Hit::class.java
            )
            binding.data=hit
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}