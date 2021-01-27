package com.honeywell.assignment.view.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.honeywell.assignment.R
import com.honeywell.assignment.api.RetrofitClient
import com.honeywell.assignment.databinding.ActivitySearchBinding
import com.honeywell.assignment.model.Hit
import com.honeywell.assignment.view.adapter.SearchAdapter
import com.honeywell.assignment.viewmodel.ArticlesViewModel
import com.honeywell.assignment.viewmodel.ArticlesViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ArticlesActivity:AppCompatActivity() {

    companion object{
        const val SEND_HIT_OBJECT="SEND_HIT_OBJECT"
    }

    lateinit var binding:ActivitySearchBinding
    lateinit var viewmodel: ArticlesViewModel

    val DIFF_CALLBACK: DiffUtil.ItemCallback<Hit> = object :
        DiffUtil.ItemCallback<Hit>(){
        override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem == newItem
        }
    }

    val adapter:SearchAdapter= SearchAdapter(DIFF_CALLBACK)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_search)
        init()
        getEvent()
    }

    private fun init(){
        val factory= ArticlesViewModelFactory(RetrofitClient.retrofit())
        viewmodel=ViewModelProvider(this,factory).get(ArticlesViewModel::class.java)
        getPagingDataAndFetchIntoRecyclerView()
    }

    private fun getEvent(){
        getSearchTextWatcherEvent()
        getSearchButtonEvent()
    }

    private fun getSearchButtonEvent(){
        binding.btnSearch.setOnClickListener {
            binding.tieSearch.text?.let {
                if (it.toString().isNotEmpty()){
                    viewmodel.query.value=it.toString()
                    CoroutineScope(Dispatchers.Main).launch {
                        adapter.submitData(PagingData.empty())
                        getPagingDataAndFetchIntoRecyclerView()
                    }
                }else{
                    binding.tilSearch.isErrorEnabled=true
                    binding.tilSearch.error=getString(R.string.enter_articles)
                }
            }
        }


    }


    private fun getSearchTextWatcherEvent(){
        binding.tieSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.isNotEmpty()){
                        binding.tilSearch.isErrorEnabled=false
                        binding.tilSearch.error=""
                    }
                }
            }

        })
    }

    private fun getPagingDataAndFetchIntoRecyclerView() {
        showOnRecyclerView()
        lifecycleScope.launch {
            viewmodel.hits().collectLatest {
                adapter.submitData(it)

            }
        }


        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading){
                if (adapter.itemCount==0){
                    binding.progressBar.visibility=View.VISIBLE
                    binding.progressBarBottom.visibility=View.GONE
                }else{
                    binding.progressBar.visibility=View.GONE
                    binding.progressBarBottom.visibility=View.VISIBLE
                }
            }
            else {
                binding.progressBar.visibility=View.GONE
                binding.progressBarBottom.visibility=View.GONE
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(this@ArticlesActivity, it.error.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }




    private fun showOnRecyclerView() {
            binding.rvArticles.layoutManager= LinearLayoutManager(this)
            binding.rvArticles.adapter=adapter
            binding.rvArticles.setHasFixedSize(true)

    }


}