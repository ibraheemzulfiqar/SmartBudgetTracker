package com.sudocipher.budget.tracker.data.datastore

import kotlinx.serialization.Serializable

@Serializable
data class Preference(
    val currencyCode: String? = null,
    val isInitialAccountSet: Boolean = false,
) {
    companion object {
        val DEFAULT = Preference(
            currencyCode = null,
            isInitialAccountSet = false,
        )

    }
}

