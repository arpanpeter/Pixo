
package com.pixaby.arpan.ui.gallery.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.pixaby.arpan.arch.api.PixabayApi
import com.pixaby.arpan.ui.gallery.paging.PixabayPagingSource
import javax.inject.Inject

class GalleryRepository @Inject constructor(private val pixabayApi: PixabayApi) {
    fun getImages(query: String) =
            Pager(
                    config = PagingConfig(
                            pageSize = 20,
                            enablePlaceholders = false,
                            prefetchDistance = 5
                    ),
                    pagingSourceFactory = { PixabayPagingSource(pixabayApi, query) }
            ).liveData
}