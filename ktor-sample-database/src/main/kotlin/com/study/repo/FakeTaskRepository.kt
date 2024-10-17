package com.study.repo

import com.study.model.Priority
import com.study.model.Task

/**
 * @Description : FakeTaskRepository.java
 * @author      : heon
 * @since       : 24. 10. 17.
 *
 * <pre>
 *
 * << 개정이력(Modification Information) >>
 *
 * ===========================================================
 * 수정인          수정자      수정내용
 * ----------    ----------    --------------------
 * 24. 10. 17.       heon         최초 생성
 *
 * <pre>
 */
class FakeTaskRepository:TaskRepository {
    private val tasks = mutableListOf(
        Task("cleaning", "Clean the house", Priority.Low),
        Task("gardening", "Mow the lawn", Priority.Medium),
        Task("shopping", "Buy the groceries", Priority.High),
        Task("painting", "Paint the fence", Priority.Medium)
    )

    override fun allTasks(): List<Task> = tasks

    override fun tasksByPriority(priority: Priority) = tasks.filter {
        it.priority == priority
    }

    override fun taskByName(name: String) = tasks.find {
        it.name.equals(name, ignoreCase = true)
    }

    override fun addTask(task: Task) {
        if (taskByName(task.name) != null) {
            throw IllegalStateException("Cannot duplicate task names!")
        }
        tasks.add(task)
    }

    override fun removeTask(name: String): Boolean {
        return tasks.removeIf { it.name == name }
    }
}