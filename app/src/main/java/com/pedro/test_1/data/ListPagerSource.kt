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
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
//DUNNO WHERE TO PUT IT, LEAVE IT HERE OR MOVE TO SOURCE