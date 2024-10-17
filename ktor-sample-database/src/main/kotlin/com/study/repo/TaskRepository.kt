package com.study.repo

import com.study.model.Priority
import com.study.model.Task

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
interface TaskRepository {
    fun allTasks(): List<Task>
    fun tasksByPriority(priority: Priority): List<Task>
    fun taskByName(name: String): Task?
    fun addTask(task: Task)
    fun removeTask(name: String): Boolean
}