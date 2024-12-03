package com.study.weather.model

/**
 * @Description : SkyCondition.java
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
enum class SkyCondition(val code: Int) {
    CLEAR(1), PARTLY_CLOUDY(3), CLOUDY(4);

    companion object {
        fun fromCode(code: Int): SkyCondition {
            return entries.find { it.code == code } ?: CLEAR
        }
    }
}
