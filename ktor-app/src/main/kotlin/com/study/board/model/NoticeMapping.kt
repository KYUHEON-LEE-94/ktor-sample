package com.study.board.model

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.*
import java.util.UUID

/**
 * @Description : mapping.java
 * @author      : heon
 * @since       : 24. 10. 21.
 *
 * <pre>
 *
 * << 개정이력(Modification Information) >>
 *
 * ===========================================================
 * 수정인          수정자      수정내용
 * ----------    ----------    --------------------
 * 24. 12. 13.       heon         최초 생성
 *
 * <pre>
 */
object NoticeMapper: IntIdTable("notice") {
    val externalId = varchar("external_id", 50).default(UUID.randomUUID().toString()) // ID를 고유한 varchar로 추가
    val title = varchar("title", 255)
    val contents = varchar("contents", 4096)
    val author = varchar("author", 50)
    val date = varchar("data", 50) // 날짜는 date로 변경하는 것이 더 적절함
}

/*
* 데이터베이스에 저장된 Notice 엔티티를 나타내는 DAO 클래스
* */
class NoticeDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NoticeDAO>(NoticeMapper)

    var externalId by NoticeMapper.externalId
    var title by NoticeMapper.title
    var contents by NoticeMapper.contents
    var author by NoticeMapper.author
    var date by NoticeMapper.date
}

/*
* DAO를 데이터 모델로 변환하는 함수
* */
fun daoToModel(dao: NoticeDAO) = Notice(
    id = dao.externalId,  // 외부 ID를 매핑
    title = dao.title,
    contents = dao.contents,
    author = dao.author,
    date = dao.date.toString() // Date 타입을 String으로 변환
)