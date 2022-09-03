
package com.pixaby.arpan.ui.preview.state

import com.pixaby.arpan.arch.mvi.IState

sealed class PreviewState: IState {
     data class LoadImage(val imageUrl:String) : PreviewState()
}