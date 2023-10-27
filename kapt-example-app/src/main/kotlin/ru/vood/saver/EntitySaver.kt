package ru.vood.saver

import kotlinx.serialization.KSerializer
import org.springframework.stereotype.Service
import ru.vood.dmgen.intf.IAggregate
import ru.vood.saver.Serialisation.jsonSerialisation

@Service
object EntitySaver {

    inline fun <reified T : IAggregate<T>> save(data: T): DataForSave {
        val serializer: KSerializer<T> = data.ktSerializer()
        return DataForSave(
            data = jsonSerialisation.encodeToString(serializer, data)
        )

    }
}

data class DataForSave(
    val data: String
)