package com.dummy.flows

import net.corda.core.identity.CordaX500Name
import net.corda.testing.common.internal.testNetworkParameters
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkNotarySpec
import net.corda.testing.node.MockNodeParameters
import net.corda.testing.node.StartedMockNode
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll

abstract class MockNetworkTest {

    private var notaryName = "O=Notary,L=London,C=GB"
    private var minimumPlatformVersion = 4

    lateinit var mockNetwork: MockNetwork
    lateinit var mockNode: StartedMockNode

    @BeforeAll
    fun setup() {
        mockNetwork = MockNetwork(
            cordappPackages = listOf(
//                "com.r3.corda.lib.tokens.workflows",
//                "com.r3.corda.lib.tokens.contracts",
//                "com.r3.corda.lib.tokens.money",
                "com.r3.corda.lib.accounts.contracts",
                "com.r3.corda.lib.accounts.workflows",
                "com.r3.corda.lib.ci.workflows",
                "com.dummy"
            ),
            notarySpecs = listOf(MockNetworkNotarySpec(CordaX500Name.parse(notaryName))),
            networkParameters = testNetworkParameters(minimumPlatformVersion = minimumPlatformVersion)
        )

        mockNode = mockNetwork.createNode(
            MockNodeParameters(legalName = CordaX500Name.parse("O=Dummy,L=London,C=GB"))
        )

        mockNetwork.runNetwork()
    }

    @AfterAll
    internal fun tearDown() {
        mockNetwork.stopNodes()
    }
}
