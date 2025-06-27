package io.github.mulgyeolpyo.stat.plugin.event

import io.github.mulgyeolpyo.stat.GlobalStatManager
import io.github.mulgyeolpyo.stat.event.StatEventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent

class StrengthStatEvent(
    manager: GlobalStatManager,
) : StatEventListener(manager = manager) {
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        stat(player).increment(1)
    }
}
