package com.example.movieactorapp.models

import android.os.Parcel
import android.os.Parcelable
import android.content.ContentValues
import com.example.movieactorapp.DatabaseHelper

data class Movie(
    val id: Int = 0,
    var title: String = "",
    var genre: String = "",
    var director: String = "",
    var duration: Int = 0,
    var releaseDate: String = "",
    var actors: List<Actor> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        title = parcel.readString() ?: "",
        genre = parcel.readString() ?: "",
        director = parcel.readString() ?: "",
        duration = parcel.readInt(),
        releaseDate = parcel.readString() ?: "",
        actors = parcel.createTypedArrayList(Actor.CREATOR) ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(genre)
        parcel.writeString(director)
        parcel.writeInt(duration)
        parcel.writeString(releaseDate)
        parcel.writeTypedList(actors)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie = Movie(parcel)
        override fun newArray(size: Int): Array<Movie?> = arrayOfNulls(size)
    }
}

// Función de extensión para convertir Movie a ContentValues
fun Movie.toContentValues(): ContentValues {
    return ContentValues().apply {
        put(DatabaseHelper.COLUMN_MOVIE_TITLE, title)
        put(DatabaseHelper.COLUMN_MOVIE_GENRE, genre)
        put(DatabaseHelper.COLUMN_MOVIE_DIRECTOR, director)
        put(DatabaseHelper.COLUMN_MOVIE_DURATION, duration)
        put(DatabaseHelper.COLUMN_MOVIE_RELEASE_DATE, releaseDate)
    }
}
