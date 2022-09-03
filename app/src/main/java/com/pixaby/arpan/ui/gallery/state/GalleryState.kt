
package com.pixaby.arpan.ui.gallery.state

import com.pixaby.arpan.arch.mvi.IState
import com.pixaby.arpan.model.Images

sealed class GalleryState : IState {
    data class GetImages(
        val imagesList: List<Images>? = null,
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    ) : GalleryState()
}