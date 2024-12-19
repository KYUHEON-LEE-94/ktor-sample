package com.study.board.service

import com.study.board.model.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SortOrder

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
        NoticeDAO.all()
            .orderBy(NoticeMapper.date to SortOrder.DESC)
            .map(::noticeDaoModel)
    }

    suspend fun allNotices(limit: Int): List<Notice> = dbSuspendTransac {
        NoticeDAO.all()
            .orderBy(NoticeMapper.date to SortOrder.DESC) // 최신 데이터가 위로 오도록 정렬
            .limit(limit) // 최대 개수 제한
            .map(::noticeDaoModel)
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

    suspend fun deleteNotice(id: Int): Int = dbSuspendTransac {
        try {
            val deleted = NoticeDAO.findById(id)?.delete() // delete()는 Unit을 반환합니다.
            val rowsDeleted = if (deleted != null) 1 else 0 // delete()가 호출되었다면 1로 취급
            println("Deleted $rowsDeleted row(s) for ID: $id")

            rowsDeleted // Int 반환

        } catch (e: Exception) {
            println("Error deleting notice: ${e.message}")
            throw e // 예외를 다시 던져서 문제를 추적
        }
    }
}