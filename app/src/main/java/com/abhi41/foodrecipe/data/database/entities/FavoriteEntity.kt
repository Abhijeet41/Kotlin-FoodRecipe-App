package com.abhi41.foodrecipe.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abhi41.foodrecipe.model.Result
import com.abhi41.foodrecipe.utils.Constants

@Entity(tableName = Constants.FAVORITE_RECIPES_TABLE)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result,

)