package com.study.util

import kotlinx.io.files.FileNotFoundException
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

class ApiKeyLoader {
    companion object {
        fun stockApiKey(): String {
            return getApiKeyByProperties("STOCK_API_KEY")
        }

        fun weatherApiKey(): String {
            return getApiKeyByProperties("WEATHER_API_KEY")
        }

        private fun getApiKeyByProperties(apiKey:String): String {
            val properties = Properties()
            val inputStream = this::class.java.classLoader.getResourceAsStream("api.properties")
                ?: throw FileNotFoundException("Resource not found: api.properties")
            properties.load(inputStream)
            return properties.getProperty(apiKey)
        }
    }
}