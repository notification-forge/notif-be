package com.forge.messageservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class MessageServiceApplication

fun main(args: Array<String>){
    runApplication<MessageServiceApplication>(*args)
}

