package com.example.expensetracker.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

class TypeConvertor {

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate):String{
        return localDate.toString()
    }
    @TypeConverter
    @RequiresApi(Build.VERSION_CODES.O)
    fun toLocalDate(str:String):LocalDate{
        return LocalDate.parse(str)
    }
    @TypeConverter
    fun fromLocalTime(time: LocalTime):String{
        return time.toString()
    }
    @TypeConverter
    @RequiresApi(Build.VERSION_CODES.O)
    fun toLocalTime(str: String):LocalTime{
        return LocalTime.parse(str)
    }
}