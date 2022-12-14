
package com.pixaby.arpan.ui.gallery.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pixaby.arpan.arch.api.PixabayApi
import com.pixaby.arpan.model.Images
import timber.log.Timber

private const val PIXABAY_STARTING_PAGE_INDEX = 1

class PixabayPagingSource(
        private val pixabayApi: PixabayApi,
        private val query: String
) : PagingSource<Int, Images>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Images> {
        val position = params.key ?: PIXABAY_STARTING_PAGE_INDEX

        return try {
            Timber.e("load Calls")
            Timber.e("position $position")
            Timber.e("params.loadSize ${params.loadSize}")
            val response = pixabayApi.getImages(query= query, page = position, perPage = params.loadSize)
            val images = response.imageList
            LoadResult.Page(
                    data = images,
                    prevKey = if (position == PIXABAY_STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if (images.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Images>): Int = PIXABAY_STARTING_PAGE_INDEX
}