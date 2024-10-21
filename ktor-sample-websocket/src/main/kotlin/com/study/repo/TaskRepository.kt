package com.study.repo

import com.study.model.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

/**
 * @Description : TaskRepository.java
 * @author      : heon
 * @since       : 24. 10. 16.
 *
 * <pre>
 *
 * << 개정이력(Modification Information) >>
 *
 * ===========================================================
 * 수정인          수정자      수정내용
 * ----------    ----------    --------------------
 * 24. 10. 16.       heon         최초 생성
 *
 * <pre>
 */
object TaskRepository {
    suspend fun allTasks(): List<Task> = suspendTransaction {
        TaskDAO.all().map(::daoToModel)
    }

    suspend fun tasksByPriority(priority: Priority): List<Task> = suspendTransaction {
        TaskDAO
            .find { TaskTable.priority eq priority.toString() }
            .map(::daoToModel)
    }

    suspend fun taskByName(name: String): Task? = suspendTransaction {
        TaskDAO
            .find { TaskTable.name eq name }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    suspend fun addTask(task: Task): Unit = suspendTransaction {
        TaskDAO.new {
            name = task.name
            description = task.description
            priority = task.priority.toString()
        }
    }

    suspend fun removeTask(name: String): Boolean = suspendTransaction {
        if(name.isEmpty()) false
        val rowsDeleted = TaskTable.deleteWhere {
            TaskTable.name eq name
        }
        rowsDeleted == 1
    }
}