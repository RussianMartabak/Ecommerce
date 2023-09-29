package com.martabak.ecommerce.main.store

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.martabak.ecommerce.main.store.data.ProductQuery
import com.martabak.ecommerce.network.ApiService
import com.martabak.ecommerce.network.data.Product

class ProductsPagingSource(
    val apiService: ApiService,
    val query: ProductQuery
) :
    PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.postProducts(
                search = query.search,
                brand = query.brand,
                highest = query.highest,
                limit = 10,
                page = nextPageNumber,
                lowest = query.lowest,
                sort = query.sort
            )
            return LoadResult.Page(
                data = response.data.items,
                prevKey = null,
                nextKey = if (nextPageNumber == response.data.totalPages) null else nextPageNumber + 1
            )
        } catch (e: Throwable) {
            Log.d("zaky", "PagingSource is throwing ${e.message!!}")
            return LoadResult.Error(e)
        }
    }
}
