package io.github.mulgyeolpyo.stat

import io.github.mulgyeolpyo.stat.config.StatConfigManager
import io.github.mulgyeolpyo.stat.event.StatEventListener
import io.github.mulgyeolpyo.stat.event.StatEventManager
import io.github.mulgyeolpyo.stat.loader.LibraryLoader
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.UUID

/**
 * The central manager for all stat-related operations within the plugin.
 * This interface provides access to stat configurations, event management, and player-specific stat data.
 * It acts as a facade for various sub-managers and handles the lifecycle of stats.
 */
@Suppress("unused")
interface GlobalStatManager {
    /**
     * Provides an accessible singleton instance of [GlobalStatManager] through a static companion object.
     * This instance is loaded via [LibraryLoader].
     */
    companion object : GlobalStatManagerInternal by LibraryLoader.loadImplement(GlobalStatManagerInternal::class.java)

    /**
     * The base directory where all stat-related data (configurations, event JARs, player data) is stored.
     */
    val dataFolder: File

    /**
     * A list of all currently registered stat identifiers (names).
     */
    val stats: List<String>

    /**
     * Provides access to the [StatConfigManager] for managing stat configurations.
     */
    val config: StatConfigManager

    /**
     * Provides access to the [StatEventManager] for managing stat-related event listeners.
     */
    val event: StatEventManager

    /**
     * A map of currently loaded [StatManager] instances, keyed by player UUID.
     * This map represents players whose stats are actively being managed in memory.
     */
    val players: Map<UUID, StatManager>

    /**
     * Registers a new stat with the system.
     * Stat names must be unique, non-blank, and entirely lowercase.
     *
     * @param stat The unique identifier (name) of the stat to register.
     * @throws IllegalArgumentException if the stat name is invalid or already exists.
     */
    fun register(stat: String)

    /**
     * Registers a new stat along with a specific [StatEventListener] class for it.
     * This method combines stat registration with the explicit registration of an event listener.
     *
     * @param stat The unique identifier (name) of the stat to register.
     * @param event The [Class] of the [StatEventListener] to associate with this stat.
     * @throws IllegalArgumentException if the stat name is invalid, already exists, or the event listener cannot be registered.
     */
    fun register(
        stat: String,
        event: Class<out StatEventListener>,
    )

    /**
     * Registers a new stat by automatically deriving its name from the provided [StatEventListener] class.
     * The stat name is derived from the event class's simple name (e.g., `StrengthStatEvent` becomes "strength").
     *
     * @param event The [Class] of the [StatEventListener] from which to derive the stat name and register.
     * @throws IllegalArgumentException if the derived stat name is invalid, already exists, or the event listener cannot be registered.
     */
    fun register(event: Class<out StatEventListener>)

    /**
     * Unregisters an existing stat from the system.
     * This removes the stat, its configurations, event listeners, and player data from management.
     *
     * @param stat The unique identifier (name) of the stat to unregister.
     * @throws IllegalArgumentException if the specified stat does not exist.
     */
    fun unregister(stat: String)

    /**
     * Unregisters a stat by automatically deriving its name from the provided [StatEventListener] class.
     * This removes the stat associated with the event listener from the system.
     *
     * @param event The [Class] of the [StatEventListener] from which to derive the stat name and unregister.
     * @throws IllegalArgumentException if the derived stat name does not exist.
     */
    fun unregister(event: Class<out StatEventListener>)

    /**
     * Creates or retrieves a [StatManager] instance for a given player, identified by their UUID.
     * This manager allows interaction with all stats for that specific player.
     *
     * @param playerId The [UUID] of the player.
     * @return A [StatManager] instance for the specified player.
     */
    fun create(playerId: UUID): StatManager

    /**
     * Creates or retrieves a [StatManager] instance for a given [Player] object.
     * This manager allows interaction with all stats for that specific player.
     *
     * @param player The [Player] object.
     * @return A [StatManager] instance for the specified player.
     */
    fun create(player: Player): StatManager

    /**
     * Loads all existing stats into the system by scanning the `dataFolder`.
     * This typically involves finding stat directories and registering each found stat.
     */
    fun load()
}

/**
 * An internal interface for creating instances of [GlobalStatManager].
 * This interface abstracts the instantiation logic, allowing the manager to be initialized
 * with or without a direct [JavaPlugin] reference.
 */
@Suppress("unused")
interface GlobalStatManagerInternal {
    /**
     * Creates a new instance of [GlobalStatManager] with a specified [JavaPlugin].
     * The plugin's data folder will be used as the default base directory for stat data.
     *
     * @param plugin The [JavaPlugin] instance associated with this manager.
     * @return A new [GlobalStatManager] instance.
     */
    fun create(plugin: JavaPlugin): GlobalStatManager

    /**
     * Creates a new instance of [GlobalStatManager] without an explicit [JavaPlugin].
     * The associated [JavaPlugin] will be automatically determined (e.g., via `JavaPlugin.getProvidingPlugin`),
     * and its data folder will be used as the base directory for stat data.
     *
     * @return A new [GlobalStatManager] instance.
     */
    fun create(): GlobalStatManager
}
