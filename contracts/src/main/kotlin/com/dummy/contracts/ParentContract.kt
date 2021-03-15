package com.dummy.contracts

import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.contracts.TypeOnlyCommandData
import net.corda.core.transactions.LedgerTransaction

class ParentContract : Contract {
    companion object {
        @JvmStatic
        val ID = "com.dummy.contracts.ParentContract"
    }

    interface Commands : CommandData {
        class Register : TypeOnlyCommandData(), Commands
    }

    override fun verify(tx: LedgerTransaction) {
    }
}

