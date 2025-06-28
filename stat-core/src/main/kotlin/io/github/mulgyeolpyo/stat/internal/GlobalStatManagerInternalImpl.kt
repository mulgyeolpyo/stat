package io.github.mulgyeolpyo.stat.internal

import io.github.mulgyeolpyo.stat.GlobalStatManager
import io.github.mulgyeolpyo.stat.GlobalStatManagerInternal
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

@Suppress("unused")
class GlobalStatManagerInternalImpl : GlobalStatManagerInternal {
    private val plugin: JavaPlugin = JavaPlugin.getProvidingPlugin(this.javaClass)

    override fun create(
        plugin: JavaPlugin,
        dataFolder: File,
    ): GlobalStatManager = GlobalStatManagerImpl(plugin, dataFolder)

    override fun create(plugin: JavaPlugin): GlobalStatManager = this.create(plugin, plugin.dataFolder)

    override fun create(dataFolder: File): GlobalStatManager = this.create(this.plugin, dataFolder)

    override fun create(): GlobalStatManager = this.create(this.plugin, this.plugin.dataFolder)

    override fun load(
        plugin: JavaPlugin,
        dataFolder: File,
    ): GlobalStatManager = this.create(plugin, dataFolder).apply { load() }

    override fun load(plugin: JavaPlugin): GlobalStatManager = this.load(plugin, plugin.dataFolder)

    override fun load(dataFolder: File): GlobalStatManager = this.load(this.plugin, dataFolder)

    override fun load(): GlobalStatManager = this.load(this.plugin, this.plugin.dataFolder)
}
