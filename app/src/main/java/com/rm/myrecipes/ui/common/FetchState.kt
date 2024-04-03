package com.rm.myrecipes.ui.common

sealed class FetchState {
    data object FetchLocal : FetchState()
    data object FetchRemote : FetchState()
    data object FetchSearch : FetchState()
}