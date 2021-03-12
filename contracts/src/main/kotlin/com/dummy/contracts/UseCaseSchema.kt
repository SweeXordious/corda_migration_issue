package com.dummy.contracts

import com.dummy.states.ParentState
import com.vladmihalcea.hibernate.type.array.ListArrayType
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne
import javax.persistence.Table
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef

object ParentContractSchema

object ParentsSchemaV1 : MappedSchema(
    schemaFamily = ParentContractSchema::class.java,
    mappedTypes = listOf(
        PersistentParent::class.java,
        PersistentChild::class.java
    ),
    version = 1
) {
    @Entity
    @Table(
        name = "parent_table",
        indexes = [
            Index(name = "parent_table_idx", columnList = "identifier")
        ]
    )
    @TypeDef(
        name = "list-array",
        typeClass = ListArrayType::class
    )
    data class PersistentParent(

        @Type(type = "uuid-char")
        @Column(name = "identifier", nullable = false)
        val identifier: UUID,

        @JoinColumns(
            JoinColumn(name = "transaction_id", referencedColumnName = "transaction_id"),
            JoinColumn(name = "output_index", referencedColumnName = "output_index")
        )
        var childs: List<PersistentChild>

        ) : PersistentState()

    @Entity
    @Suppress("unused")
    @Table(name = "child_table")
    data class PersistentChild(

        @Column(name = "name", nullable = false)
        val name: String,

        @Column(name = "type", nullable = false)
        val type: String,

        @ManyToOne(
            targetEntity = PersistentParent::class,
            fetch = FetchType.LAZY
        )
        var parent: ParentState

    ) : PersistentState()
}
