package com.study.board.model

import com.study.util.getCurrentDateFormatted
import kotlinx.serialization.Serializable

/**
 * @Description : Notice.java
 * @author      : heon
 * @since       : 24. 12. 10.
 *
 * <pre>
 *
 * << 개정이력(Modification Information) >>
 *
 * ===========================================================
 * 수정인          수정자      수정내용
 * ----------    ----------    --------------------
 * 24. 12. 10.       heon         최초 생성
 *
 * <pre>
 */
@Serializable
data class Notice(var id: String ="", var title: String = "", var contents: String = "", var author: String = "", var date: String = getCurrentDateFormatted())
