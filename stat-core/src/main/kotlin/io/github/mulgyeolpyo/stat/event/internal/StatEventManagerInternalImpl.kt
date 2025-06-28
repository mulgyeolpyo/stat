package io.github.mulgyeolpyo.stat.event.internal

import io.github.mulgyeolpyo.stat.GlobalStatManager
import io.github.mulgyeolpyo.stat.event.StatEventManager
import io.github.mulgyeolpyo.stat.event.StatEventManagerInternal
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

@Suppress("unused")
class StatEventManagerInternalImpl : StatEventManagerInternal {
    val plugin = JavaPlugin.getProvidingPlugin(this.javaClass)

    override fun create(
        manager: GlobalStatManager,
        plugin: JavaPlugin,
        dataFolder: File,
    ): StatEventManager = StatEventManagerImpl(manager, plugin, dataFolder)

    override fun create(
        manager: GlobalStatManager,
        plugin: JavaPlugin,
    ): StatEventManager = this.create(manager, plugin, plugin.dataFolder)

    override fun create(
        manager: GlobalStatManager,
        dataFolder: File,
    ): StatEventManager = this.create(manager, this.plugin, dataFolder)

    override fun create(manager: GlobalStatManager): StatEventManager = this.create(manager, this.plugin, this.plugin.dataFolder)
}
