package com.example.util

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

object DateUtils {

    const val MILLIS_IN_DAY = 86400000

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

    fun calculateDaysUntilNextBirthday(birthdayTimestamp: Long): Int {
        require(birthdayTimestamp > 0) { "Некорректная временная метка: $birthdayTimestamp" }
        val birthDate = LocalDate.ofEpochDay(birthdayTimestamp / MILLIS_IN_DAY)
        val today = LocalDate.now()

        val nextBirthday = birthDate.withYear(today.year)

        // Если ДР прошёл в этом году - берём следующий год
        val adjustedBirthday = if (nextBirthday.isBefore(today)) {
            nextBirthday.plusYears(1)
        } else {
            nextBirthday
        }

        // Разница в днях
        return ChronoUnit.DAYS.between(today, adjustedBirthday).toInt()
    }
}
