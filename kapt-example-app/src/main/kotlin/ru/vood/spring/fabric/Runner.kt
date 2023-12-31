package ru.vood.spring.fabric

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum
import ru.vood.dmgen.datamodel.runtime.dataclasses.DealEntity
import ru.vood.dmgen.meta.toDto
import ru.vood.dmgen.meta.toJson

@Component
class Runner(
    val JsoSerializationFabric: JsoSerializationFabric,
    val JsonSerializer: JsonSerializer
) : CommandLineRunner {
    override fun run(vararg args: String?) {
//        val toJson = DealEntity(1).toJson()

//        val toDto = toDto<DealEntity>(toJson)

        val encodeToString = JsonSerializer.json.encodeToString(TSome.serializer(), TSome("asd"))


//        println(JsonSerializer.serial<TSome>(encodeToString))

        val runtimeClass = DataDictionaryEntityEnum.Deal.runtimeClass
//        DataDictionaryEntityEnum.Deal.entitySerializer<DataDictionaryEntityEnum.Deal.runtimeClass>()

    }

    @kotlinx.serialization.Serializable
    data class TSome(val q: String)
}