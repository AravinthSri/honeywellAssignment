package com.honeywell.assignment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.honeywell.assignment.api.HoneyWellServices
import com.honeywell.assignment.datasource.SearchDatasource
import com.honeywell.assignment.model.Hit
import kotlinx.coroutines.flow.Flow

class ArticlesViewModel(val honeyWellServices: HoneyWellServices):ViewModel() {

     var query=MutableLiveData<String>()

    fun hits(): Flow<PagingData<Hit>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { SearchDatasource(honeyWellServices,query.value.toString()) }
        ).flow.cachedIn(viewModelScope)
    }




}