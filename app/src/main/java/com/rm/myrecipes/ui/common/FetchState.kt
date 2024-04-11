package com.rm.myrecipes.ui.common

import com.rm.myrecipes.ui.utils.EMPTY_STRING

sealed class FetchState {
    data object FetchLocal : FetchState()

    data object FetchRemote : FetchState()

    data object FetchSearch : FetchState() {
        var data: String = EMPTY_STRING
    }
}