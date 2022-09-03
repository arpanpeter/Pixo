
package com.pixaby.arpan.ui.gallery.action

import com.pixaby.arpan.arch.mvi.IAction

sealed class GalleryActions: IAction {
    data class GetImages(val query:String): GalleryActions()
}