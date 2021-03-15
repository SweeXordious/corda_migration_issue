package com.dummy.flows

import com.dummy.states.ParentState
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import com.natpryce.hamkrest.isA
import org.junit.jupiter.api.Test

class ParentRegisterFlowsTest : MockNetworkTest() {

    @Test
    fun `register parent case`() {

        val signedTransaction = mockNode.startFlow(
            RegisterParentFlow(
                triggers = PARKING_START_TRIGGER
            )
        )

        mockNetwork.runNetwork()

        with(signedTransaction.get().coreTransaction) {
            assertThat(outputStates, hasSize(equalTo(1)))
            assertThat(outputStates.single(), isA<ParentState>())

            val parentState = outputStates[0] as ParentState

            with(parentState) {
                assertThat(triggers, equalTo(PARKING_START_TRIGGER))
            }
        }
    }

    companion object {
        val PARKING_START_TRIGGER = setOf("parking" to "start_time")
    }
}
