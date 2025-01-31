package com.example.movieactorapp.models

import android.content.ContentValues
import android.os.Parcel
import android.os.Parcelable

data class Actor(
    val id: Int = 0,
    var name: String = "",
    var age: Int = 0,
    var nationality: String = "",
    var isOscarWinner: Boolean = false,
    var salary: Double = 0.0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        name = parcel.readString() ?: "",
        age = parcel.readInt(),
        nationality = parcel.readString() ?: "",
        isOscarWinner = parcel.readByte() != 0.toByte(),
        salary = parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(age)
        parcel.writeString(nationality)
        parcel.writeByte(if (isOscarWinner) 1 else 0)
        parcel.writeDouble(salary)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Actor> {
        override fun createFromParcel(parcel: Parcel): Actor = Actor(parcel)
        override fun newArray(size: Int): Array<Actor?> = arrayOfNulls(size)
    }
}


// Función de extensión para convertir Actor a ContentValues
fun Actor.toContentValues(): ContentValues {
    return ContentValues().apply {
        put("name", name)
        put("age", age)
        put("nationality", nationality)
        put("is_oscar_winner", if (isOscarWinner) 1 else 0)
        put("salary", salary)
    }
}
