package com.example.playlistmaker.search_tracks

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    @SerializedName("artworkUrl100")
    val artworkUrl: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(trackId)
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeLong(trackTimeMillis)
        parcel.writeString(artworkUrl)
        parcel.writeString(collectionName)
        parcel.writeString(releaseDate)
        parcel.writeString(primaryGenreName)
        parcel.writeString(country)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }

    fun getCoverArtwork() = artworkUrl.replaceAfterLast("/", "512x512bb.jpg")

}
