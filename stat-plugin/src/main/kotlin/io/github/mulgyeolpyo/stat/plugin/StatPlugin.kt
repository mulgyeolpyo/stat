package io.github.mulgyeolpyo.stat.plugin

// import io.github.mulgyeolpyo.stat.GlobalStatManager
// import io.github.mulgyeolpyo.stat.plugin.event.StrengthStatEvent
// import net.kyori.adventure.text.Component
// import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class StatPlugin : JavaPlugin() {
    override fun onEnable() {
//        val manager = GlobalStatManager.create(this)
//        manager.register(StrengthStatEvent::class.java)
//
//        server.scheduler.runTaskTimer(
//            this,
//            Runnable {
//                Bukkit.getOnlinePlayers().forEach { player ->
//                    val statManager = manager.create(player)
//                    val strengthConfig = manager.config.get("strength")
//                    val strengthLv = statManager.level("strength")
//                    val strengthValue = statManager.get("strength")
//                    player.sendActionBar {
//                        Component.text(
//                            "strength: Lv.$strengthLv ($strengthValue/${strengthConfig.levels[strengthLv]})",
//                        )
//                    }
//                }
//            },
//            0L,
//            1L,
//        )
//
//        logger.info("StatPlugin has been enabled.")
    }
}
