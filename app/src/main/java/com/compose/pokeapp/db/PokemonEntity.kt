package com.compose.pokeapp.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(
    tableName = "pokemon_list",
    indices = [Index(value = ["id"], unique = true)]
)
@Parcelize
data class PokemonEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "url")
    var url: String = "",
): Parcelable