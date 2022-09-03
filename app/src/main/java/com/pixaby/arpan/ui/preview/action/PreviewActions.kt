
package com.pixaby.arpan.ui.preview.action

import com.pixaby.arpan.arch.mvi.IAction

sealed class PreviewActions: IAction {
    data class LoadImage(val imageUrl:String): PreviewActions()
}