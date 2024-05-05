package com.rm.myrecipes.ui.common

import com.rm.myrecipes.ui.utils.EMPTY_STRING

sealed class FetchType {
    data object Local : FetchType()

    data object Remote : FetchType()

    data object Search : FetchType() {
        var data: String = EMPTY_STRING
    }
}