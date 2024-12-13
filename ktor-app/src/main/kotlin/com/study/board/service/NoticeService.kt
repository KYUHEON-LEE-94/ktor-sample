package com.study.board.service

import com.study.board.model.*
import org.jetbrains.exposed.sql.SchemaUtils

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

object NoticeService {

    suspend fun allNotices(): List<Notice> = dbSuspendTransac {
        NoticeDAO .all().map(::noticeDaoModel)
    }

    suspend fun saveNotice (notice: Notice): Unit = dbSuspendTransac {
        try {
            println("insert Notice $notice")

            NoticeDAO.new {
                title = notice.title
                contents = notice.contents
                author = notice.author
                date = notice.date
            }
        } catch (e: Exception) {
            println("Error saving notice: ${e.message}")
            throw e // 예외를 다시 던져서 문제를 추적
        }
    }
}