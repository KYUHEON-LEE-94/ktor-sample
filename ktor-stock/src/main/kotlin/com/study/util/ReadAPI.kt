package com.study.util

import java.io.FileInputStream
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

fun loadApiKey(): String {
    val properties = Properties()
    val inputStream = FileInputStream("src/main/resources/api.properties")
    properties.load(inputStream)
    return properties.getProperty("API_KEY")
}