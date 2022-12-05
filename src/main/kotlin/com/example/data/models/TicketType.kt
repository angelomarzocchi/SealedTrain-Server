package com.example.data.models

enum class TicketType(val expiringTime: Long) { //expiring time in milliseconds
    ONE_DAY(86400000){
        override fun toString(): String {
            return "One Day"
        }
                     },
    SEVEN_DAYS(604800000){
        override fun toString(): String {
            return "Seven Days"
        }
                         },
    FULL_YEAR(28771200000){
        override fun toString(): String {
            return "One Year"
        }
    };


}