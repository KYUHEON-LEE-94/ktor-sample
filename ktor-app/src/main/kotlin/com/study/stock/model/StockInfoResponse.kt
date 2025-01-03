package com.study.stock.model

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
data class StockInfoResponse(
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
    val basDt: String = "",        // 기준 날짜
    val srtnCd: String = "",       // 단축 코드
    val isinCd: String = "",       // 국제 증권 식별 번호ㅎ
    val itmsNm: String = "",       // 종목명
    val mrktCtg: String = "",      // 시장 구분 (예: KOSDAQ, KOSPI)
    val clpr: String = "",         // 종가 (Close Price)
    val vs: String = "",           // 대비 (전일 대비 가격 변화)
    val fltRt: String = "",        // 등락률
    val mkp: String = "",          // 시가 (Market Price)
    val hipr: String = "",         // 고가 (High Price)
    val lopr: String = "",         // 저가 (Low Price)
    val trqu: String = "",         // 거래량
    val trPrc: String = "",        // 거래 대금
    val lstgStCnt: String = "",    // 상장 주식수
    val mrktTotAmt: String = ""    // 시가총액
)
