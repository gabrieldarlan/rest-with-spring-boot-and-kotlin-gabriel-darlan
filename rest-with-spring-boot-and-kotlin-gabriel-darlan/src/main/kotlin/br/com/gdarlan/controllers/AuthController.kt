package br.com.gdarlan.controllers

import br.com.gdarlan.data.vo.v1.AccountCredentialsVO
import br.com.gdarlan.services.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Authentication EndPoint")
@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    private lateinit var authService: AuthService

    @Operation(summary = "Authenticates an user and return a token")
    @PostMapping(value = ["/signin"])
    fun signin(@RequestBody data: AccountCredentialsVO?): ResponseEntity<*> {
        return if (data!!.userName.isNullOrBlank() || data.password.isNullOrBlank())
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request")
        else authService.signin(data)
    }

    @Operation(summary = "Refresh token for authenticated user and returns a token")
    @PutMapping(value = ["/refresh/{username}"])
    fun refreshToken(
        @PathVariable("username") userName: String?,
        @RequestHeader("Authorization") refreshToken: String?,
    ): ResponseEntity<*> {
        return if (refreshToken.isNullOrBlank() || userName.isNullOrBlank())
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request")
        else authService.refreshToken(userName, refreshToken)
//        var ok = ""
//        return ResponseEntity.status(HttpStatus.OK).body("Invalid client request")
    }


}