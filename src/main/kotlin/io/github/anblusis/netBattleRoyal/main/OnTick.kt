package io.github.anblusis.netBattleRoyal.main

import io.github.monun.tap.fake.FakeEntityServer

class OnTick(
    private val fakeEntityManager: FakeEntityServer,
): Runnable {
    override fun run() {
        fakeEntityManager.update()
    }
}