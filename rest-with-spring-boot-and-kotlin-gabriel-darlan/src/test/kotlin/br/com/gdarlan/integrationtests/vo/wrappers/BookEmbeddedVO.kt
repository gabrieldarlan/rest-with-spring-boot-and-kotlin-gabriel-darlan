package br.com.gdarlan.integrationtests.vo.wrappers

import br.com.gdarlan.integrationtests.vo.BookVO
import com.fasterxml.jackson.annotation.JsonProperty

class BookEmbeddedVO {
    @JsonProperty("bookVOList")
    var books: List<BookVO>? = null
}
