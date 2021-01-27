package com.honeywell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.honeywell.assignment.api.HoneyWellServices


@Suppress("UNCHECKED_CAST")
class ArticlesViewModelFactory(val honeyWellServices: HoneyWellServices):ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ArticlesViewModel(honeyWellServices) as T
    }


}