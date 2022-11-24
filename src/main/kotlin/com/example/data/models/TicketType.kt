package com.example.data.models

enum class TicketType(val expiringTime: Long) { //expiring time in milliseconds
    ONE_DAY(86400000),
    SEVEN_DAYS(604800000),
    FULL_YEAR(28771200000)
}