package br.com.gdarlan.mapper.custom

import br.com.gdarlan.data.vo.v1.PersonVO
import br.com.gdarlan.model.Person
import org.springframework.stereotype.Service
import java.util.*

@Service
class PersonMapper {

    fun mapEntityToVO(person: Person): PersonVO {
        return PersonVO(
            key = person.id,
            address = person.address,
//            birthDay = Date(),
            firstName = person.firstName,
            lastName = person.lastName,
            gender = person.gender,
        )
    }

    fun mapVOToEntity(personVO: PersonVO): Person {
        return Person(
            id = personVO.key,
            address = personVO.address,
//            birthDay = Date(),
            firstName = personVO.firstName,
            lastName = personVO.lastName,
            gender = personVO.gender,
        )
    }


}