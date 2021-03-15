package com.dummy.flows

import co.paralleluniverse.fibers.Suspendable
import com.dummy.contracts.ParentContract
import com.dummy.states.ParentState
import net.corda.core.contracts.Command
import net.corda.core.contracts.StateAndContract
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.FinalityFlow
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker

@StartableByRPC
@InitiatingFlow
class RegisterParentFlow(
    private val triggers: Set<Pair<String, String>>
) : FlowLogic<SignedTransaction>() {

    override val progressTracker: ProgressTracker = tracker()

    @Suspendable
    override fun call(): SignedTransaction {

        progressTracker.currentStep = REGISTERING

        val parentState = ParentState(
            identifier = UniqueIdentifier(),
            triggers = triggers,
            host = ourIdentity
        )

        val stateAndContract = StateAndContract(parentState, ParentContract::class.java.canonicalName)
        val command = Command(ParentContract.Commands.Register(), listOf(ourIdentity.owningKey))
        val notary = serviceHub.networkMapCache.notaryIdentities.single()

        val utx = TransactionBuilder(notary = notary)
            .withItems(stateAndContract, command)

        progressTracker.currentStep = SIGNING

        val transaction = serviceHub.signInitialTransaction(utx, command.signers)
        subFlow(FinalityFlow(transaction, listOf(), FINALISING.childProgressTracker()))

        progressTracker.currentStep = FINALISING

        return transaction

    }

    companion object {
        object REGISTERING : ProgressTracker.Step("Registering Parent state")
        object FETCHING : ProgressTracker.Step("Fetching Linear State")
        object SIGNING : ProgressTracker.Step("Signing Linear State")
        object FINALISING : ProgressTracker.Step("Sending Linear State") {
            override fun childProgressTracker() = FinalityFlow.tracker()
        }

        @JvmStatic
        fun tracker() = ProgressTracker(
            FETCHING,
            REGISTERING,
            SIGNING,
            FINALISING
        )
    }
}
