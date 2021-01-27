package com.honeywell.assignment.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.honeywell.assignment.R
import com.honeywell.assignment.databinding.SearchItemBinding
import com.honeywell.assignment.model.Hit
import com.honeywell.assignment.utils.hideKeyboard
import com.honeywell.assignment.view.activity.ArticlesActivity
import com.honeywell.assignment.view.activity.ArticlesDetails


class SearchAdapter(DIFF_CALLBACK: DiffUtil.ItemCallback<Hit>):PagingDataAdapter<Hit, SearchAdapter.SearchViewHolder>(
    DIFF_CALLBACK
) {


    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item: Hit? = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding:SearchItemBinding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.search_item,
            parent,
            false
        )
        return SearchViewHolder(binding)
    }


    inner class SearchViewHolder(val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(searchItem: Hit?) {
            searchItem?.let {
                binding.tvTitle.text = it.title
            }

            binding.root.setOnClickListener {
                binding.root.context.hideKeyboard(binding.root)
                val gson = Gson()
                val searchData = gson.toJson(searchItem)
                val intent = Intent(binding.root.context,ArticlesDetails::class.java)
                intent.putExtra(ArticlesActivity.SEND_HIT_OBJECT, searchData)
                binding.root.context.startActivity(intent)
            }
        }
    }
}