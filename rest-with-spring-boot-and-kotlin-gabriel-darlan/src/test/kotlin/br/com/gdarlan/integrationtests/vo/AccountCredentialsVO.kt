package br.com.gdarlan.integrationtests.vo

import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement
data class AccountCredentialsVO(
    var userName: String? = null,
    var password: String? = null,
) {
}