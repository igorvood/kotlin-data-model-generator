package kaptexample.app

import ru.vood.dmgen.datamodel.runtime.dataclasses.DealEntity
import ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamOneToOneEntity
import ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamOneToOneOptionalEntity
import ru.vood.dmgen.intf.IContextOf
import ru.vood.dmgen.intf.IEntity

interface IContextExtractor<T : IEntity<T>> {

    fun extract(data: T) : IContextOf<T>
}


class asd: IContextExtractor<DealEntity>{
    override fun extract(data: DealEntity): DealContextDeal_PK {
        val value: IContextExtractor<DealEntity> = object : IContextExtractor<DealEntity> {
            override fun extract(data: DealEntity) = DealContextDeal_PK(data.id)
        }

        val extract = value.extract(
            DealEntity(
                1,
                DealParamOneToOneOptionalEntity(1, ""),
                DealParamOneToOneEntity(1, "asd"),
                setOf()
            )
        )


        return DealContextDeal_PK(1)
    }
}

