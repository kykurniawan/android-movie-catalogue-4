package com.rizky.submission.last.favoriteapp.movie

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieItems(
    var id: Int = 0,
    var title: String? = null,
    var releaseDate: String? = null,
    var overview: String? = null,
    var posterPath: String? = null,
    var voteCount: Int? = null,
    var popularity: Int? = 0,
    var backdropPath: String? = null,
    var voteAverage: Int? = 0
) : Parcelable