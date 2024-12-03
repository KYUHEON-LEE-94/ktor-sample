package com.study.weather.model

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
    val dataType: String = "",
    val pageNo: Long = 0,
    val numOfRows: Long = 0,
    val totalCount: Long = 0,
    val items: Items = Items()
)
@Serializable
data class Items(
    val item: List<WeatherItem> = listOf()
)
@Serializable
data class WeatherItem(
    val baseDate:String = "",
    val baseTime:String = "",
    var category: String= "",
    var nx:Int = 0,
    var ny:Int = 0,
    var obsrValue:String = ""
)
