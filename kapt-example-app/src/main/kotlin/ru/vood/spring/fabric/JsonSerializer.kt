package ru.vood.spring.fabric

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
object JsonSerializer {

    val json: Json = Json {

    }


    inline fun <reified T : Any> serial(data: String): T {

        return json.decodeFromString(data)

    }

}
