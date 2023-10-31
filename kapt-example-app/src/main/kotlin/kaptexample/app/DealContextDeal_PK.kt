package kaptexample.app
                    
@kotlinx.serialization.Serializable
data class DealContextDeal_PK (
val id: kotlin.Int
): ru.vood.dmgen.intf.IContextOf<ru.vood.dmgen.datamodel.runtime.dataclasses.DealEntity>
{
override val metaEntity: ru.vood.dmgen.intf.IMetaEntity
        get() = ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum.Deal
        
override fun ktSerializer() = serializer()        
}          
                    