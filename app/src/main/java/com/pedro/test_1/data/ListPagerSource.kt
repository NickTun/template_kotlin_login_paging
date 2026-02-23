package com.pedro.test_1.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pedro.test_1.data.repo.FakeRepository
import com.pedro.test_1.domain.entities.ListItem

class ListPagerSource(
    val backend: FakeRepository,
    val pageSize: Int
) : PagingSource<Int, ListItem>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, ListItem> {
        val nextPageNumber = params.key ?: 1
        val response = backend.fetchItems(page = nextPageNumber, pageSize = pageSize)
        return LoadResult.Page(
            data = response.data,
            prevKey = null, // Only paging forward.
            nextKey = response.nextPage
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ListItem>): Int? {
        // Try to find the page key of the closest page to anchorPosition from
        // either the prevKey or the nextKey; you need to handle nullability
        // here.
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey are null -> anchorPage is the
        //    initial page, so return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}