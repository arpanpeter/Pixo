
package com.pixaby.arpan.arch.mvi

interface IView<S: IState> {
    fun render(state: S)
}