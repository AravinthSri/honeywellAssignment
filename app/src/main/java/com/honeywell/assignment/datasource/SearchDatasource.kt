package com.honeywell.assignment.datasource

import androidx.paging.PagingSource
import com.honeywell.assignment.api.HoneyWellServices
import com.honeywell.assignment.model.Hit
import com.honeywell.assignment.model.SearchResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

@Suppress("UNCHECKED_CAST")
class SearchDatasource(val services: HoneyWellServices, val query:String): PagingSource<Int, Hit>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hit> {
        try {
            var finalList:ArrayList<Hit> = ArrayList()
            val currentLoadingPageKey = params.key ?: 1
            val scope = CoroutineScope(Dispatchers.IO)
            val data=scope.async(Dispatchers.IO) {
                getSearchArticlesFromAPI(query,currentLoadingPageKey.toString()).apply {
                    return@async this?.hits
                }
            }
            val userData=data.await()
            if (userData!=null){
                if (userData !is Int){
                    finalList = userData as ArrayList<Hit>
                }else{
                    finalList = ArrayList()
                }
            }
            if (!finalList.isNullOrEmpty()){
                val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1
                return LoadResult.Page(
                    data = finalList,
                    prevKey = prevKey,
                    nextKey = currentLoadingPageKey.plus(1)
                )
            }else{

                return LoadResult.Page(
                    data = finalList,
                    prevKey = null,
                    nextKey = null
                )
            }



        }catch (e:Exception){
            e.printStackTrace()
            return LoadResult.Error(e)
        }
    }


    private fun getSearchArticlesFromAPI(query: String,page:String): SearchResponse? {
        var searchResponse: SearchResponse?
        runBlocking {
            val scope = CoroutineScope(Dispatchers.IO)
            val data=scope.async(Dispatchers.IO) {
                try {
                    val locRes=services.getArticles(query,page)
                    val response=locRes.execute()

                    if (response.body()!=null){
                        searchResponse=response.body()
                    }else{
                        searchResponse = null
                    }

                    return@async searchResponse

                }catch (e:Exception){
                    e.printStackTrace()
                    return@async null
                }
            }
            runBlocking {
                searchResponse = data.await()
            }
        }

        return  searchResponse
    }

}