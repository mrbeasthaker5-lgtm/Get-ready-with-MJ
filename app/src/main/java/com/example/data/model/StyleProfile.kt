package com.example.data.model

enum class GenderStylePreference(val displayName: String, val subtitle: String) {
    BOY("Boy", "Masculine & sharp styling cuts"),
    GIRL("Girl", "Feminine & layered aesthetics"),
    UNSPECIFIED("Prefer Not to Say", "Universal, fluid styling recommendations")
}

data class StyleProfile(
    val genderStylePreference: GenderStylePreference = GenderStylePreference.UNSPECIFIED
)
