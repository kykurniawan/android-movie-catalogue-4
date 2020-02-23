package com.rizky.submission.last.moviecatalogue.ui.tvshow

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class TvShowItems(
    var id: Int = 0,
    var name: String? = null,
    var firstAirDate: String? = null,
    var overview: String? = null,
    var posterPath: String? = null,
    var voteCount: Int? = null,
    var popularity: Int? = 0,
    var backdropPath: String? = null,
    var voteAverage: Int? = 0
) : Parcelable