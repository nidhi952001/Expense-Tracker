package com.example.expensetracker.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

class TypeConvertor {



    @TypeConverter
    fun fromColor(color: Color):Int{
        return color.hashCode()
    }
    @TypeConverter
    fun toColor(int:Int):Color{
        return Color(int)
    }
}