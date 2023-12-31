package ru.vood.spring.fabric

import kotlinx.serialization.KSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum
import ru.vood.dmgen.datamodel.runtime.dataclasses.DealEntity
import ru.vood.dmgen.intf.IEntity
import ru.vood.spring.fabric.JsonSerializer.json
import javax.annotation.PostConstruct

@Component
object JsoSerializationFabric {

    @Autowired
    lateinit var asdad: JsonSerializer

    lateinit var serializer: Map<DataDictionaryEntityEnum, JsonSerializer>


    @PostConstruct
    fun init() {

    }

//    inline fun <reified T : IEntity<T>> T.toJson(): String {
//        return json.encodeToString(this.ktSerializer(), this)
//    }

    inline fun <reified T : IEntity<T>> DataDictionaryEntityEnum.fromJson(string: String): T {
        val entitySerializer = this.entitySerializer<T>()
        return json.decodeFromString(entitySerializer, string)
    }


    inline fun <reified T : Any> serial(
        type: DataDictionaryEntityEnum, data: String,
//                                      kClass: KClass<T>
    ): T {

        val serializer1: KSerializer<DealEntity> = DealEntity.serializer()

//        val designClass = type.designClass as KClass<T>

//        val serial = serializer[type]!!.serial(data, designClass)
//        return serial
        TODO()
    }


}