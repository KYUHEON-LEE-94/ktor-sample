package com.study.util

import kotlin.math.*

/**
 * @Description : GpsTransfer.java
 * @author      : heon
 * @since       : 24. 12. 2.
 *
 * <pre>
 *
 * << 개정이력(Modification Information) >>
 *
 * ===========================================================
 * 수정인          수정자      수정내용
 * ----------    ----------    --------------------
 * 24. 12. 2.       heon         최초 생성
 *
 * <pre>
 */
class GpsTransfer(
    var lat: Double = 0.0, // GPS 위도
    var lon: Double = 0.0, // GPS 경도
    var xLat: Double = 0.0, // X 좌표로 변환된 위도
    var yLon: Double = 0.0  // Y 좌표로 변환된 경도
) {

    companion object {
        private const val RE = 6371.00877 // 지구 반경(km)
        private const val GRID = 5.0 // 격자 간격(km)
        private const val SLAT1 = 30.0 // 투영 위도1(degree)
        private const val SLAT2 = 60.0 // 투영 위도2(degree)
        private const val OLON = 126.0 // 기준점 경도(degree)
        private const val OLAT = 38.0 // 기준점 위도(degree)
        private const val XO = 43.0 // 기준점 X 좌표(GRID)
        private const val YO = 136.0 // 기준점 Y 좌표(GRID)

        private const val TO_GRID = 0
        private const val TO_GPS = 1

        private const val DEGRAD = Math.PI / 180.0
        private const val RADDEG = 180.0 / Math.PI
    }

    /**
     * GPS 좌표와 X,Y 좌표 변환
     */
    fun transfer(target: GpsTransfer, mode: Int) {
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD

        val sn = ln(cos(slat1) / cos(slat2)) / ln(tan(Math.PI * 0.25 + slat2 * 0.5) / tan(Math.PI * 0.25 + slat1 * 0.5))
        val sf = tan(Math.PI * 0.25 + slat1 * 0.5).pow(sn) * cos(slat1) / sn
        val ro = re * sf / tan(Math.PI * 0.25 + olat * 0.5).pow(sn)

        when (mode) {
            TO_GRID -> {
                // GPS -> X, Y 좌표
                val ra = re * sf / tan(Math.PI * 0.25 + target.lat * DEGRAD * 0.5).pow(sn)
                var theta = target.lon * DEGRAD - olon
                theta = normalizeTheta(theta) * sn

                target.xLat = floor(ra * sin(theta) + XO + 0.5)
                target.yLon = floor(ro - ra * cos(theta) + YO + 0.5)
            }
            TO_GPS -> {
                // X, Y 좌표 -> GPS
                val xn = target.xLat - XO
                val yn = ro - target.yLon + YO
                val ra = sqrt(xn * xn + yn * yn).let { if (sn < 0.0) -it else it }
                val alat = 2.0 * atan((re * sf / ra).pow(1.0 / sn)) - Math.PI * 0.5

                val theta = atan2(xn, yn)
                val alon = theta / sn + olon

                target.lat = alat * RADDEG
                target.lon = alon * RADDEG
            }
        }
    }

    /**
     * 각도를 -π ~ π 범위로 조정
     */
    private fun normalizeTheta(theta: Double): Double {
        return when {
            theta > Math.PI -> theta - 2.0 * Math.PI
            theta < -Math.PI -> theta + 2.0 * Math.PI
            else -> theta
        }
    }

    override fun toString(): String {
        return "GpsTransfer(lat=$lat, lon=$lon, xLat=$xLat, yLon=$yLon)"
    }
}