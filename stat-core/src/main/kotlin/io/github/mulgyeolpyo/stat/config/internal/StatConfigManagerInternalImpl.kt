package io.github.mulgyeolpyo.stat.config.internal

import io.github.mulgyeolpyo.stat.GlobalStatManager
import io.github.mulgyeolpyo.stat.config.StatConfigManager
import io.github.mulgyeolpyo.stat.config.StatConfigManagerInternal
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

@Suppress("unused")
class StatConfigManagerInternalImpl : StatConfigManagerInternal {
    override fun create(
        manager: GlobalStatManager,
        plugin: JavaPlugin,
    ): StatConfigManager = this.create(manager, plugin.dataFolder)

    override fun create(
        manager: GlobalStatManager,
        dataFolder: File,
    ): StatConfigManager = StatConfigManagerImpl(manager, dataFolder)

    override fun create(manager: GlobalStatManager): StatConfigManager = this.create(manager, manager.dataFolder)
}
