package ru.vood.spring.fabric

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum

import javax.annotation.PostConstruct
import kotlin.reflect.KClass

@Component
object JsoSerializationFabric {

    @Autowired
    lateinit var     asdad: JsonSerializer

    lateinit var serializer: Map<DataDictionaryEntityEnum, JsonSerializer>


    @PostConstruct
    fun init(){

    }

    inline fun<reified T: Any> serial(type: DataDictionaryEntityEnum, data: String,
//                                      kClass: KClass<T>
    ): T{

//        val designClass = type.designClass as KClass<T>

//        val serial = serializer[type]!!.serial(data, designClass)
//        return serial
        TODO()
    }


}