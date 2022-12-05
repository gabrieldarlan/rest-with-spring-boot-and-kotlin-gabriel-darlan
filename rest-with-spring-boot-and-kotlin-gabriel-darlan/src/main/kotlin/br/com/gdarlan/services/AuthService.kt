package br.com.gdarlan.services


import br.com.gdarlan.data.vo.v1.AccountCredentialsVO
import br.com.gdarlan.data.vo.v1.TokenVO
import br.com.gdarlan.repository.UserRepository
import br.com.gdarlan.security.jwt.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class AuthService {
    @Autowired
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var tokenProvider: JwtTokenProvider

    private val logger = Logger.getLogger(AuthService::class.java.name)

    fun signin(data: AccountCredentialsVO): ResponseEntity<*> {
        logger.info("Trying logger user ${data.userName}")
        return try {
            val userName = data.userName
            val password = data.password
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(userName, password))
            val user = repository.findByUserName(userName)
            val tokenResponse: TokenVO = if (user != null) {
                tokenProvider.createAccessToken(userName!!, user.roles)
            } else {
                throw UsernameNotFoundException("Username $userName not found!")
            }
            ResponseEntity.ok(tokenResponse)
        } catch (e: AuthenticationException) {
            throw BadCredentialsException("Invalid username or password supplied!")
        }
    }

    fun refreshToken(userName: String, refreshToken: String): ResponseEntity<*> {
        logger.info("Trying get refresh user ${userName}")

        val user = repository.findByUserName(userName)
        val tokenResponse: TokenVO = if (user != null) {
            tokenProvider.refreshToken(refreshToken)
        } else {
            throw UsernameNotFoundException("Username $userName not found!")
        }
        return ResponseEntity.ok(tokenResponse)
    }
}