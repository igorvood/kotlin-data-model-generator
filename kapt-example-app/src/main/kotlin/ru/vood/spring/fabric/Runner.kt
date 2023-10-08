package ru.vood.spring.fabric

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class Runner(
    val JsoSerializationFabric: JsoSerializationFabric,
    val JsonSerializer: JsonSerializer
    ):CommandLineRunner {
    override fun run(vararg args: String?) {

        val encodeToString = JsonSerializer.json.encodeToString(TSome.serializer(), TSome("asd"))


        println(JsonSerializer.serial<TSome>(encodeToString))


    }

    @kotlinx.serialization.Serializable
    data class TSome(val q: String)
}