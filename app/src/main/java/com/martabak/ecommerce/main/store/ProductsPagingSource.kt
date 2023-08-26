package com.martabak.ecommerce.main.store

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.martabak.ecommerce.network.data.Product
import com.martabak.ecommerce.repository.StoreRepository


class ProductsPagingSource(val storeRepository: StoreRepository) :
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
            val response = storeRepository.getProducts(nextPageNumber)
            return LoadResult.Page(
                data = response.data.items,
                prevKey = null,
                nextKey = if (response.data.pageIndex == response.data.totalPages) null else response.data.pageIndex + 1
            )
        } catch (e: Throwable) {
            Log.d("zaky", "PagingSource is throwing ${e.message!!}")
            return LoadResult.Error(e)
        }
    }


}