package com.study.model

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

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
 * 24. 10. 21.       heon         최초 생성
 *
 * <pre>
 */
object TaskTable: IntIdTable("task") {
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val priority = varchar("priority", 50)
}

/*
* 데이터베이스에 저장된 Task 엔티티를 나타내는 객체
* */
class TaskDAO(id: EntityID<Int>) : IntEntity(id) {
    //Exposed에서 TaskDAO를 관리하는 역할을 하는 클래스
    companion object : IntEntityClass<TaskDAO>(TaskTable)

    //이 변수들은 TaskTable의 컬럼에 매핑됩니다.
    // 예를 들어, TaskDAO 객체의 name 속성을 읽거나 쓰면, 이는 자동으로 데이터베이스의 name 컬럼과 연결
    var name by TaskTable.name
    var description by TaskTable.description
    var priority by TaskTable.priority
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
//비동기 트랜잭션을 실행하는 Exposed 라이브러리 함수
    // 이 함수는 코루틴 내에서 일시 중단 가능한 방식으로 트랜잭션을 실행
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun daoToModel(dao: TaskDAO) = Task(
    dao.name,
    dao.description,
    Priority.valueOf(dao.priority)
)