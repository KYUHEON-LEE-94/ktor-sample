package com.study.model.weather

import com.study.util.ApiKeyLoader.Companion.stockApiKey
import com.study.util.ApiKeyLoader.Companion.weatherApiKey
import com.study.util.getCurrentDateFormatted
import kotlinx.serialization.Serializable

/**
 * @Description : StockPriceRequest.java
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
@Serializable
data class WeatherRequest(
    val serviceKey: String = weatherApiKey(),
    val numOfRows: Int = 1000, //한 페이지 결과 수
    val pageNo: Int = 1, //페이지 번호
    val dataType: String = "json",
    val baseDate:String = getCurrentDateFormatted(),
    var nx:Int = 0,
    var ny:Int = 0
    )
