package com.example.mvvmarch.presentation.main.create

sealed class CreateMainFragmentState {
    object Init: CreateMainFragmentState()
    data class IsLoading(val isLoading: Boolean): CreateMainFragmentState()
    data class ShowToast(val message: String): CreateMainFragmentState()
    object SuccessCreate: CreateMainFragmentState()
}
