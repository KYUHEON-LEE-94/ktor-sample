package com.study.weather.model

/**
 * @Description : PrecipitationType.java
 * @author      : heon
 * @since       : 24. 12. 3.
 *
 * <pre>
 *
 * << 개정이력(Modification Information) >>
 *
 * ===========================================================
 * 수정인          수정자      수정내용
 * ----------    ----------    --------------------
 * 24. 12. 3.       heon         최초 생성
 *
 * <pre>
 */
/**
 * 강수형태(PTY) 코드 정의
 */
enum class PrecipitationType(val code: Int, val description: String) {
    NONE(0, "없음"),          // 강수 없음
    RAIN(1, "비"),            // 비
    RAIN_SNOW(2, "비/눈"),    // 비와 눈
    SNOW(3, "눈"),            // 눈
    DRIZZLE(5, "빗방울"),     // 빗방울
    DRIZZLE_SNOW(6, "빗방울/눈날림"), // 빗방울과 눈날림
    SNOW_FLURRY(7, "눈날림"); // 눈날림

    companion object {
        fun fromCode(code: Int): PrecipitationType {
            return entries.find { it.code == code } ?: NONE
        }
    }
}
