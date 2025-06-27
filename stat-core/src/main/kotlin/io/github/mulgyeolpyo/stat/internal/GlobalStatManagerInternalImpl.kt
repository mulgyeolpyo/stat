package io.github.mulgyeolpyo.stat.internal

import io.github.mulgyeolpyo.stat.GlobalStatManager
import io.github.mulgyeolpyo.stat.GlobalStatManagerInternal
import org.bukkit.plugin.java.JavaPlugin

class GlobalStatManagerInternalImpl : GlobalStatManagerInternal {
    override fun create(plugin: JavaPlugin): GlobalStatManager = GlobalStatManagerImpl(plugin)

    override fun create(): GlobalStatManager = GlobalStatManagerImpl()
}
