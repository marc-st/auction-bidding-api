package com.example.auction.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value= HttpStatus.NOT_FOUND)
class ItemNotFoundException(override val message: String?) : Exception(message)
