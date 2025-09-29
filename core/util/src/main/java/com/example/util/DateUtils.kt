package com.example.util

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object DateUtils {

    fun dateToTimestamp(date: String): Long {
        requireNotNull(date) { "Дата не может быть null" }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return try {
            LocalDate.parse(date, formatter)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Неверный формат даты. Ожидаемый формат: yyyy-MM-dd", e)
        } catch (e: Exception) {
            throw RuntimeException("Ошибка при преобразовании даты", e)
        }
    }
}
