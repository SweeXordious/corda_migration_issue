package com.dummy.states

import com.dummy.contracts.ParentContract
import com.dummy.contracts.ParentsSchemaV1
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.Party
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState

@BelongsToContract(ParentContract::class)
class ParentState (

    /**
     * Unique identifier within the Corda network.
     */
    val identifier: UniqueIdentifier,

    /**
     * Set of Name ➔ type relationships (probably with intelligence on the device).
     */
    val triggers: Set<Pair<String, String>>,
    /**
     * Use case owning party.
     */
    val host: Party

) : QueryableState {

    /**
     * No participant restrictions.
     */
    override val participants = listOf(host)

    override fun generateMappedObject(schema: MappedSchema): PersistentState {
        if (schema is ParentsSchemaV1) {
            return ParentsSchemaV1.PersistentParent(
                identifier = identifier.id,
                childs = triggers.map {
                    ParentsSchemaV1.PersistentChild(
                        name = it.first,
                        type = it.second,
                        parent = this
                    )
                }
            )
        } else {
            throw IllegalStateException("Cannot construct instance of ${this.javaClass} from Schema: $schema")
        }
    }

    override fun supportedSchemas() = listOf(ParentsSchemaV1)
}
