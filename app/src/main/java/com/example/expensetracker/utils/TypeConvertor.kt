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
    fun fromLocalTime(time: LocalTime):String{
        return time.toString()
    }
    @TypeConverter
    @RequiresApi(Build.VERSION_CODES.O)
    fun toLocalTime(str: String):LocalTime{
        return LocalTime.parse(str)
    }

    @TypeConverter
    fun fromColor(color: Color):Int{
        return color.hashCode()
    }
    @TypeConverter
    fun toColor(int:Int):Color{
        return Color(int)
    }
}