package ru.vood.dmgen

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DmGenApplication

fun main(args: Array<String>) {
    runApplication<DmGenApplication>(*args)
}
