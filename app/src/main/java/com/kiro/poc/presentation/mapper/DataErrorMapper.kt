package com.kiro.poc.presentation.mapper

import com.kiro.poc.R
import com.kiro.poc.common.UiText
import com.kiro.poc.data.error.DataError

fun DataError.toUiText(): UiText {
    return when (this) {
        is DataError.NoInternet -> UiText.StringResource(R.string.error_no_internet)
        is DataError.Serialization -> UiText.StringResource(R.string.error_serialization)
        is DataError.Unknown -> UiText.DynamicString(this.message ?: "Unknown error")
        is DataError.Network -> {
            if (this.message != null) {
                UiText.DynamicString(this.message)
            } else {
                UiText.StringResource(R.string.error_network, this.code ?: 0, "Unknown")
            }
        }
    }
}
