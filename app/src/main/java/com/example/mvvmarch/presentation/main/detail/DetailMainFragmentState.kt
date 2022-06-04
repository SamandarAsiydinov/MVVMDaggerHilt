package com.example.mvvmarch.presentation.main.detail

sealed class DetailMainFragmentState {
    object Init: DetailMainFragmentState()
    data class IsLoading(val isLoading: Boolean): DetailMainFragmentState()
    object SuccessDelete : DetailMainFragmentState()
    object SuccessUpdate : DetailMainFragmentState()
    data class ShowToast(val message: String): DetailMainFragmentState()
}
