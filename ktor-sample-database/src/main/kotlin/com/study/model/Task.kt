package com.study.model

import kotlinx.serialization.Serializable

/**
 * @Description : Task.java
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
enum class Priority {
    Low, Medium, High, Vital
}

@Serializable
data class Task(
    val name: String,
    val description: String,
    val priority: Priority
)