package com.sudocipher.budget.tracker.data.datastore

import kotlinx.serialization.Serializable

@Serializable
data class Preference(
    val showWelcomeMessage: Boolean,
) {
    companion object {
        val DEFAULT = Preference(
            showWelcomeMessage = true,
        )

    }
}

