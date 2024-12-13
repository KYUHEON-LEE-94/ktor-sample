package com.study.board.service

import com.study.board.model.Notice
import com.study.board.model.NoticeDAO
import com.study.util.GpsTransfer
import com.study.util.getCurrentTimeFormatted
import com.study.weather.model.PrecipitationType
import com.study.weather.model.WeatherRequest
import com.study.weather.model.WeatherResponse
import kotlinx.serialization.json.Json
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @Description : WeatherService.java
 * @author      : heon
 * @since       : 24. 11. 29.
 *
 * <pre>
 *
 * << 개정이력(Modification Information) >>
 *
 * ===========================================================
 * 수정인          수정자      수정내용
 * ----------    ----------    --------------------
 * 24. 11. 29.       heon         최초 생성
 *
 * <pre>
 */

class NoticeService {


    fun saveNotice(notice:Notice): Notice {
        println("insert Notice $notice")
        return transaction {
            val newNotice = NoticeDAO.new {
                title = notice.title
                contents = notice.contents
                author = notice.author
                date = notice.date
            }

            Notice(
                title = newNotice.title,
                contents = newNotice.contents,
                author = newNotice.author,
                date = newNotice.date
            )

        }


        }
}