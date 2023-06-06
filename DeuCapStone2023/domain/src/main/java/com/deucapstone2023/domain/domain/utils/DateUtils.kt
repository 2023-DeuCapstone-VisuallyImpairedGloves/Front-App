package com.deucapstone2023.domain.domain.utils

import java.util.Calendar

var Calendar.year
    get() = get(Calendar.YEAR)
    set(value) {
        set(Calendar.YEAR, value)
    }

var Calendar.month
    get() = get(Calendar.MONTH) + 1
    set(value) {
        set(Calendar.MONTH, value - 1)
    }

var Calendar.date
    get() = get(Calendar.DATE)
    set(value) {
        set(Calendar.DATE, value)
    }

var Calendar.hour
    get() = get(Calendar.HOUR)
    set(value) {
        set(Calendar.HOUR, value)
    }

var Calendar.minute
    get() = get(Calendar.MINUTE)
    set(value) {
        set(Calendar.MINUTE, value)
    }

var Calendar.second
    get() = get(Calendar.SECOND)
    set(value) {
        set(Calendar.SECOND, value)
    }

fun getCurrentTime() = run {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
    }
    "${calendar.year}/${calendar.month}/${calendar.date} ${calendar.hour}-${calendar.minute}-${calendar.second}"
}
