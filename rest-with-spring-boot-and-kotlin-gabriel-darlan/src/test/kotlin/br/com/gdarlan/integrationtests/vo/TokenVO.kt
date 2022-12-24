package br.com.gdarlan.integrationtests.vo

import java.util.*
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement
data class TokenVO(
    var userName: String? = null,
    var authenticated: Boolean? = null,
    var created:Date? = null,
    var expiration: Date? = null,
    var accessToken: String? = null,
    var refreshToken: String? = null,
) {
}