package com.example.auction.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
class InvalidItemException(override val message: String?) : Exception(message)
