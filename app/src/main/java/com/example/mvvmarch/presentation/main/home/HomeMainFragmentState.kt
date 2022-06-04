package com.example.mvvmarch.presentation.main.home

sealed class HomeMainFragmentState {
    object Init: HomeMainFragmentState()
    data class IsLoading(val isLoading: Boolean): HomeMainFragmentState()
    data class ShoToast(val message: String): HomeMainFragmentState()
}