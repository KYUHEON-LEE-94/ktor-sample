package com.study.model

import com.study.util.ApiKeyLoader.Companion.loadApiKey
import java.time.LocalDate

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
data class StockInfoRequest(
    val serviceKey: String = loadApiKey(),
    val numOfRows: Int = 100, //한 페이지 결과 수
    val pageNo: Int = 1, //페이지 번호
    val resultType: String = "json",
    val basDt:LocalDate = LocalDate.now(),
    val beginBasDt:LocalDate = LocalDate.now().minusYears(1),
    val endBasDt:LocalDate = LocalDate.now(),
    val likeBasDt:LocalDate = LocalDate.now(),
    val likeSrtnCd:String = "",
    val isinCd:String = "",
    val likeIsinCd:String = "",
    val itmsNm:String = "", //검색값과 종목명이 일치하는 데이터
    val likeItmsNm:String = "", //종목명이 검색값을 포함하는 데이터
    val mrktCls:String = "", //검색값과 시장구분이 일치하는 데이터
    val beginVs:String = "", //대비가 검색값보다 크거나 같은 데이터
    val endVs:String = "", //대비가 검색값보다 작은 데이터
    val beginFltRt:String = "", //등락률이 검색값보다 크거나 같은 데이터
    val endFltRt:String = "", //등락률이 검색값보다 작은 데이터
    val beginTrqu:Long = 0L, //거래량이 검색값보다 크거나 같은 데이터
    val endTrqu:Long = 0L, //거래량이 검색값보다 작은 데이터
    val beginTrPrc:Long = 0L, //거래대금이 검색값보다 크거나 같은 데이터
    val endTrPrc:Long = 0L, //거래대금이 검색값보다 작은 데이터
    val beginLstgStCnt:Long = 0L, //상장주식수가 검색값보다 크거나 같은 데이터
    val endLstgStCnt:Long = 0L, //상장주식수가 검색값보다 작은 데이터
    val beginMrktTotAmt:Long = 0L, //시가총액이 검색값보다 크거나 같은 데이터
    val endMrktTotAmt:Long = 0L //시가총액이 검색값보다 작은 데이터
    )
