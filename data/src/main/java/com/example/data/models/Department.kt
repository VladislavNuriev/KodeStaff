package com.example.data.models

enum class Department(val stringName: String) {
    Android("Android"),
    Ios("iOS"),
    Design("Дизайн"),
    Management("Менеджмент"),
    Qa("QA"),
    BackOffice("Бэк-офис"),
    Frontend("Frontend"),
    Hr("HR"),
    Pr("PR"),
    Backend("Backend"),
    Support("Техподдержка"),
    Analytics("Аналитика");


    companion object {
        fun fromString(stringName: String): Department {
            return entries.find { it.stringName == stringName } ?: Android
        }
    }
}