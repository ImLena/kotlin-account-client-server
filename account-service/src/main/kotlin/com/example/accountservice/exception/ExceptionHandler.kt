package com.example.accountservice.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler
    fun handleIllegalStateException(ex: NullPointerException): ResponseEntity<String> {
        return ResponseEntity("Account not found", HttpStatus.NOT_FOUND)
    }
}
