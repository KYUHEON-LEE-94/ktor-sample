package com.study.model.weather

import com.study.util.ApiKeyLoader.Companion.weatherApiKey
import com.study.util.getCurrentDateFormatted
import kotlinx.serialization.Serializable

/**
 * @Description : StockInfoResponse.java
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
data class WeatherResponse(
    val response: Response = Response()
)
@Serializable
data class Response(
    val header: Header = Header(),
    val body: Body = Body(),
)
@Serializable
data class Header(
    val resultCode: String = "",
    val resultMsg: String = ""
)
@Serializable
data class Body(
    val numOfRows: Int = 0,
    val pageNo: Int = 0,
    val totalCount: Int = 0,
    val items: Items = Items()
)
@Serializable
data class Items(
    val item: List<StockInfoItem> = listOf()
)
@Serializable
data class StockInfoItem(
    val numOfRows: Int = 1000, //한 페이지 결과 수
    val pageNo: Int = 1, //페이지 번호
    val dataType: String = "json",
    val baseDate:String = "",
    val nx:Int = 0,
    val ny:Int = 0
)
