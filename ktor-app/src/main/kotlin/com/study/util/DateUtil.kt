package com.study.util

import kotlinx.io.files.FileNotFoundException
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Properties

/**
 * @Description : loadApiKey.java
 * @author      : heon
 * @since       : 24. 11. 11.
 *
 * <pre>
 *
 * << 개정이력(Modification Information) >>
 *
 * ===========================================================
 * 수정인          수정자      수정내용
 * ----------    ----------    --------------------
 * 24. 11. 11.       heon         최초 생성
 *
 * <pre>
 */

fun getCurrentDateFormatted(): String {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    return today.format(formatter)
}

fun getCurrentTimeFormatted(): String {
    val currentTime  = LocalTime.now().withMinute(0).withSecond(0)
    val formatter = DateTimeFormatter.ofPattern("HHmm")
    return currentTime.format(formatter)
}