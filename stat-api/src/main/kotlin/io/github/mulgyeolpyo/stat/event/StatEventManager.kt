package io.github.mulgyeolpyo.stat.event

import io.github.mulgyeolpyo.stat.GlobalStatManager
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * Manages the registration, loading, and enabling/disabling of stat-specific event listeners.
 * This manager is responsible for handling [StatEventListener] classes, including those loaded from external JAR files.
 */
interface StatEventManager {
    companion object : StatEventManagerInternal by io.github.mulgyeolpyo.stat.loader.LibraryLoader.loadImplement(
        StatEventManagerInternal::class.java,
    )

    /**
     * Registers and enables event listeners for a specific stat.
     * This method typically attempts to load event listener classes from an external JAR file
     * associated with the given stat, and then enables them.
     *
     * @param stat The unique identifier of the stat for which to register listeners.
     * @throws IllegalArgumentException if the specified stat does not exist in [GlobalStatManager].
     */
    fun register(stat: String)

    /**
     * Registers and enables a specific [StatEventListener] class for a given stat.
     * The provided event listener class will be instantiated and registered with the Bukkit plugin manager.
     *
     * @param stat The unique identifier of the stat to associate with this listener.
     * @param event The [Class] of the [StatEventListener] to register.
     * @throws IllegalArgumentException if the specified stat does not exist in [GlobalStatManager].
     * @throws IllegalArgumentException if the listener cannot be instantiated (e.g., missing constructor).
     */
    fun register(
        stat: String,
        event: Class<out StatEventListener>,
    )

    /**
     * Registers and enables a [StatEventListener] class, automatically determining the stat name.
     * The stat name is derived from the event class's simple name (e.g., `StrengthStatEvent` becomes "strength").
     *
     * @param event The [Class] of the [StatEventListener] to register.
     * @throws IllegalArgumentException if the derived stat name does not exist in [GlobalStatManager].
     * @throws IllegalArgumentException if the listener cannot be instantiated (e.g., missing constructor).
     */
    fun register(event: Class<out StatEventListener>)

    /**
     * Unregisters all event listeners associated with a specific stat.
     * This disables any currently active listeners for that stat and removes their classes from internal tracking.
     *
     * @param stat The unique identifier of the stat whose listeners are to be unregistered.
     * @throws IllegalArgumentException if the specified stat does not exist in [GlobalStatManager].
     */
    fun unregister(stat: String)

    /**
     * Enables all currently registered event listeners for a specific stat.
     * This instantiates and registers listeners that were previously loaded but might have been disabled.
     *
     * @param stat The unique identifier of the stat whose listeners are to be enabled.
     * @throws IllegalArgumentException if the specified stat does not exist in [GlobalStatManager].
     * @throws IllegalArgumentException if any listener cannot be instantiated.
     */
    fun enable(stat: String)

    /**
     * Disables all currently active event listeners for a specific stat.
     * This unregisters them from the Bukkit plugin manager, but does not remove their classes from internal tracking.
     *
     * @param stat The unique identifier of the stat whose listeners are to be disabled.
     * @throws IllegalArgumentException if the specified stat does not exist in [GlobalStatManager].
     */
    fun disable(stat: String)

    /**
     * Loads [StatEventListener] classes for a specific stat from an external JAR file.
     * The JAR file is expected to be located at `dataFolder/stat/event.jar`.
     * This method only loads the classes into memory; it does not automatically enable them.
     *
     * @param stat The unique identifier of the stat for which to load event listeners.
     * @return A list of [StatEventListener] classes found and loaded for the specified stat.
     * @throws IllegalArgumentException if the specified stat does not exist in [GlobalStatManager].
     */
    fun load(stat: String): List<Class<out StatEventListener>>

    /**
     * Loads event listeners for all registered stats from their respective external JAR files.
     * This iterates through all stats known to [GlobalStatManager] and calls [load] for each.
     */
    fun load()
}

/**
 * An internal interface for creating instances of [StatEventManager].
 * This interface abstracts the instantiation logic for the event manager,
 * providing various ways to initialize it with different data storage configurations.
 */
interface StatEventManagerInternal {
    /**
     * Creates a new instance of [StatEventManager] with explicit control over the plugin and data folder.
     *
     * @param manager The [GlobalStatManager] instance to associate with this event manager.
     * @param plugin The [JavaPlugin] instance responsible for registering Bukkit events.
     * @param dataFolder The base directory where event-related files (like JARs) will be stored.
     * @return A new [StatEventManager] instance.
     */
    fun create(
        manager: GlobalStatManager,
        plugin: JavaPlugin,
        dataFolder: File,
    ): StatEventManager

    /**
     * Creates a new instance of [StatEventManager] using the provided plugin's default data folder.
     * This is a convenience method that calls [create] with `plugin.dataFolder` as the `dataFolder`.
     *
     * @param manager The [GlobalStatManager] instance to associate with this event manager.
     * @param plugin The [JavaPlugin] instance whose data folder will be used.
     * @return A new [StatEventManager] instance.
     */
    fun create(
        manager: GlobalStatManager,
        plugin: JavaPlugin,
    ): StatEventManager

    /**
     * Creates a new instance of [StatEventManager] with a specific data folder, using a default plugin.
     * The plugin context will be automatically determined (e.g., via `JavaPlugin.getProvidingPlugin`).
     *
     * @param manager The [GlobalStatManager] instance to associate with this event manager.
     * @param dataFolder The base directory where event-related files (like JARs) will be stored.
     * @return A new [StatEventManager] instance.
     */
    fun create(
        manager: GlobalStatManager,
        dataFolder: File,
    ): StatEventManager

    /**
     * Creates a new instance of [StatEventManager] using the default plugin and data folder.
     * The plugin context and data folder will be determined automatically (e.g., `JavaPlugin.getProvidingPlugin`
     * and `GlobalStatManager.dataFolder`).
     *
     * @param manager The [GlobalStatManager] instance, which also provides the default data folder.
     * @return A new [StatEventManager] instance.
     */
    fun create(manager: GlobalStatManager): StatEventManager
}
