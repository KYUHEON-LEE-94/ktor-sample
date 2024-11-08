package com.study.model

import kotlinx.serialization.Serializable

/**
 * @Description : TaskMessage.java
 * @author      : heon
 * @since       : 24. 10. 28.
 *
 * <pre>
 *
 * << 개정이력(Modification Information) >>
 *
 * ===========================================================
 * 수정인          수정자      수정내용
 * ----------    ----------    --------------------
 * 24. 10. 28.       heon         최초 생성
 *
 * <pre>
 */
@Serializable
data class TaskMessage(val command: String, val task: Task?)
