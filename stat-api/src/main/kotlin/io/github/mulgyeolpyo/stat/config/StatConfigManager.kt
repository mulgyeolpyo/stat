package io.github.mulgyeolpyo.stat.config

import io.github.mulgyeolpyo.stat.GlobalStatManager
import io.github.mulgyeolpyo.stat.loader.LibraryLoader
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * Manages the loading, saving, and retrieval of stat configurations for various stats.
 * This manager handles the persistence of [StatConfig] objects to and from files.
 */
interface StatConfigManager {
    /**
     * Provides an accessible singleton instance of [StatConfigManager] through a static companion object.
     * This instance is loaded via [LibraryLoader].
     */
    companion object : StatConfigManagerInternal by LibraryLoader.loadImplement(StatConfigManagerInternal::class.java)

    /**
     * Unregisters and removes the configuration for a specific stat from the manager.
     * Before removal, if the stat's configuration is currently loaded, it attempts to save it.
     *
     * @param stat The unique identifier of the stat to unregister.
     * @throws IllegalArgumentException if the specified stat does not exist in [GlobalStatManager].
     */
    fun unregister(stat: String)

    /**
     * Retrieves the [StatConfig] for a given stat.
     * If the configuration is not already loaded into memory, it will attempt to load it from disk.
     *
     * @param stat The unique identifier of the stat whose configuration is to be retrieved.
     * @return The [StatConfig] instance associated with the specified stat.
     * @throws IllegalArgumentException if the specified stat does not exist in [GlobalStatManager].
     */
    fun get(stat: String): StatConfig

    /**
     * Sets or updates the [StatConfig] for a specific stat in memory.
     * This operation does not immediately persist the changes to disk; [save] must be called for that.
     *
     * @param stat The unique identifier of the stat whose configuration is to be set.
     * @param config The [StatConfig] instance to associate with the stat.
     * @throws IllegalArgumentException if the specified stat does not exist in [GlobalStatManager].
     */
    fun set(
        stat: String,
        config: StatConfig,
    )

    /**
     * Loads the configuration for a single specific stat from its corresponding file on disk.
     * The loaded configuration replaces any existing configuration in memory for that stat.
     *
     * @param stat The unique identifier of the stat whose configuration is to be loaded.
     * @return The newly loaded [StatConfig] instance.
     * @throws IllegalArgumentException if the specified stat does not exist in [GlobalStatManager].
     */
    fun load(stat: String): StatConfig

    /**
     * Loads configurations for all registered stats from their respective files on disk.
     * This typically iterates through all stats known to [GlobalStatManager] and calls [load] for each.
     */
    fun load()

    /**
     * Saves the in-memory configuration of a specific stat to its corresponding file on disk.
     * This overwrites any existing file for that stat.
     *
     * @param stat The unique identifier of the stat whose configuration is to be saved.
     * @throws IllegalArgumentException if the specified stat does not exist in [GlobalStatManager].
     * @throws IllegalStateException if the configuration for the specified stat is not found in memory (should not happen under normal operation if previously loaded or set).
     */
    fun save(stat: String)

    /**
     * Saves all currently managed stat configurations from memory to their respective files on disk.
     * This iterates through all configurations currently held by the manager and calls [save] for each.
     */
    fun save()
}

/**
 * An internal interface for creating instances of [StatConfigManager].
 * This interface abstracts the instantiation logic, allowing different ways
 * to initialize the manager (e.g., with a plugin's data folder or a custom file path).
 */
interface StatConfigManagerInternal {
    /**
     * Creates a new instance of [StatConfigManager] using the data folder of a provided [JavaPlugin].
     * Stat configurations will be stored within the plugin's data directory.
     *
     * @param manager The [GlobalStatManager] instance to associate with this config manager.
     * @param plugin The [JavaPlugin] whose data folder will be used for storing stat configurations.
     * @return A new [StatConfigManager] instance.
     */
    fun create(
        manager: GlobalStatManager,
        plugin: JavaPlugin,
    ): StatConfigManager

    /**
     * Creates a new instance of [StatConfigManager] using a specified base data folder.
     * Stat configurations will be stored within a "stat" subdirectory inside this provided `dataFolder`.
     *
     * @param manager The [GlobalStatManager] instance to associate with this config manager.
     * @param dataFolder The base directory where stat configuration files will be stored.
     * @return A new [StatConfigManager] instance.
     */
    fun create(
        manager: GlobalStatManager,
        dataFolder: File,
    ): StatConfigManager

    /**
     * Creates a new instance of [StatConfigManager] using the default data folder provided by the [GlobalStatManager].
     *
     * @param manager The [GlobalStatManager] instance, which also provides the default data folder.
     * @return A new [StatConfigManager] instance.
     */
    fun create(manager: GlobalStatManager): StatConfigManager
}
