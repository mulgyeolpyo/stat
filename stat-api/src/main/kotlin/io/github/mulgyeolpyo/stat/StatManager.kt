package io.github.mulgyeolpyo.stat

/**
 * Manages individual stat values for a specific player.
 * This interface provides methods to get, set, modify, and persist a player's stats.
 * Each instance of [StatManager] is typically associated with one player's stat data.
 */
interface StatManager {
    /**
     * Unregisters a specific stat from this manager.
     * If the stat's value is currently loaded in memory, it will be saved before removal.
     *
     * @param stat The unique identifier of the stat to unregister.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun unregister(stat: String)

    /**
     * Retrieves the current value of a specific stat for the managed player.
     * If the stat value is not yet loaded into memory, it will be loaded from persistent storage.
     *
     * @param stat The unique identifier of the stat to retrieve.
     * @return The current float value of the stat.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun get(stat: String): Float

    /**
     * Calculates and returns the level corresponding to the current value of a specific stat.
     * The level is determined based on the stat's configuration.
     *
     * @param stat The unique identifier of the stat whose level is to be determined.
     * @return The calculated integer level of the stat.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun level(stat: String): Int

    /**
     * Sets the value of a specific stat for the managed player.
     * This updates the stat in memory; call [save] to persist changes.
     *
     * @param stat The unique identifier of the stat to set.
     * @param value The new float value for the stat.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun set(
        stat: String,
        value: Float,
    )

    /**
     * Sets the value of a specific stat for the managed player, converting an integer to a float.
     * This updates the stat in memory; call [save] to persist changes.
     *
     * @param stat The unique identifier of the stat to set.
     * @param value The new integer value for the stat, which will be converted to float.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun set(
        stat: String,
        value: Int,
    )

    /**
     * Increases the value of a specific stat for the managed player by a float amount.
     * This updates the stat in memory; call [save] to persist changes.
     *
     * @param stat The unique identifier of the stat to increment.
     * @param value The float amount to add to the current stat value.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun increment(
        stat: String,
        value: Float,
    )

    /**
     * Increases the value of a specific stat for the managed player by an integer amount.
     * This updates the stat in memory; call [save] to persist changes.
     *
     * @param stat The unique identifier of the stat to increment.
     * @param value The integer amount to add to the current stat value, which will be converted to float.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun increment(
        stat: String,
        value: Int,
    )

    /**
     * Decreases the value of a specific stat for the managed player by a float amount.
     * This updates the stat in memory; call [save] to persist changes.
     *
     * @param stat The unique identifier of the stat to decrement.
     * @param value The float amount to subtract from the current stat value.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun decrement(
        stat: String,
        value: Float,
    )

    /**
     * Decreases the value of a specific stat for the managed player by an integer amount.
     * This updates the stat in memory; call [save] to persist changes.
     *
     * @param stat The unique identifier of the stat to decrement.
     * @param value The integer amount to subtract from the current stat value, which will be converted to float.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun decrement(
        stat: String,
        value: Int,
    )

    /**
     * Loads the value of a specific stat for the managed player from persistent storage into memory.
     * If the player's data for this stat is not found, it defaults to the stat's configured random initial value.
     *
     * @param stat The unique identifier of the stat to load.
     * @return The loaded float value of the stat.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun load(stat: String): Float

    /**
     * Loads the values of all registered stats for the managed player from persistent storage into memory.
     * This typically iterates through all stats known to the global manager and calls [load] for each.
     */
    fun load()

    /**
     * Saves the current in-memory value of a specific stat for the managed player to persistent storage.
     *
     * @param stat The unique identifier of the stat to save.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun save(stat: String)

    /**
     * Saves the current in-memory values of all managed stats for the player to persistent storage.
     * This typically iterates through all currently known stat values and calls [save] for each.
     */
    fun save()
}
