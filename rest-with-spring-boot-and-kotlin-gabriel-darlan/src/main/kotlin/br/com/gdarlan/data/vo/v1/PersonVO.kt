package br.com.gdarlan.data.vo.v1

import com.github.dozermapper.core.Mapping
import org.springframework.hateoas.RepresentationModel

//@JsonPropertyOrder("id", "address","first_name", "last_name","gender")
data class PersonVO(
    @Mapping("id")
    var key: Long = 0,
//    @field:JsonProperty("first_name")
    var firstName: String = "",
//    @field:JsonProperty("last_name")
    var lastName: String = "",
    var address: String = "",
//    @field:JsonIgnore
    var gender: String = "",
) : RepresentationModel<PersonVO>()

