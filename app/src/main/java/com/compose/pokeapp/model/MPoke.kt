package com.compose.pokeapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class MPoke(
    @Exclude var id: String? = null,
    var name: String? = null,
    var authors: String? = null,
    @get:PropertyName("photo_url")
    @set:PropertyName("photo_url")
    var photoUrl: String? = null,
    var categories: String? = null,
)