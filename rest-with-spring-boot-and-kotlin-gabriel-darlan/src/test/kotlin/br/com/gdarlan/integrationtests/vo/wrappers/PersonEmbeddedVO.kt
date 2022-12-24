package br.com.gdarlan.integrationtests.vo.wrappers

import br.com.gdarlan.integrationtests.vo.PersonVO
import com.fasterxml.jackson.annotation.JsonProperty

class PersonEmbeddedVO {
    @JsonProperty("personVOList")
    var persons: List<PersonVO>? = null
}
