package com.study.model

import com.study.util.loadApiKey
import java.time.LocalDate

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
data class StockInfoResponse(
    val resultCode: String = loadApiKey(),       // API 호출 결과 상태 코드 (예: "00")
    val resultMsg: String,        // API 호출 결과 상태 (예: "NORMAL SERVICE.")
    val numOfRows: Int,           // 한 페이지 결과 수 (예: 1)
    val pageNo: Int,              // 페이지 번호 (예: 1)
    val totalCount: Int,          // 전체 결과 수 (예: 1713576)
    val basDt: LocalDate,         // 기준 일자 (예: 2022-09-19)
    val srtnCd: String,           // 단축 코드 (예: "900110")
    val isinCd: String,           // ISIN 코드 (예: "HK0000057197")
    val itmsNm: String,           // 종목명 (예: "이스트아시아홀딩스")
    val mrktCtg: String,          // 시장 구분 (예: "KOSDAQ")
    val clpr: Int,                // 종가 (예: 167)
    val vs: Int,                  // 전일 대비 (예: -8)
    val fltRt: Double,            // 등락률 (예: -4.57)
    val mkp: Int,                 // 시가 (예: 173)
    val hipr: Int,                // 고가 (예: 176)
    val lopr: Int,                // 저가 (예: 167)
    val trqu: Long,               // 거래량 (예: 2788311)
    val trPrc: Long,              // 거래대금 (예: 475708047)
    val lstgStCnt: Long,          // 상장 주식수 (예: 219932050)
    val mrktTotAmt: Long          // 시가총액 (예: 36728652350)
)
