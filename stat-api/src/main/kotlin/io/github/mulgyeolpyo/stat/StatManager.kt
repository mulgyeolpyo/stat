package io.github.mulgyeolpyo.stat

/**
 * Manages individual stat values for a specific player.
 * This interface provides methods to get, set, modify, and persist a player's stats.
 * Each instance of [StatManager] is typically associated with one player's stat data.
 */
@Suppress("unused")
interface StatManager {
    /**
     * Unregisters a specific stat from this manager.
     * If the stat's exp is currently loaded in memory, it will be saved before removal.
     *
     * @param stat The unique identifier of the stat to unregister.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun unregister(stat: String)

    /**
     * Retrieves the current exp of a specific stat for the managed player.
     * If the stat exp is not yet loaded into memory, it will be loaded from persistent storage.
     *
     * @param stat The unique identifier of the stat to retrieve.
     * @return The current Long exp of the stat.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun get(stat: String): Long

    /**
     * Calculates and returns the level corresponding to the current exp of a specific stat.
     * The level is determined based on the stat's configuration.
     *
     * @param stat The unique identifier of the stat whose level is to be determined.
     * @return The calculated integer level of the stat.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun level(stat: String): Int

    /**
     * Sets the exp of a specific stat for the managed player.
     * This updates the stat in memory; call [save] to persist changes.
     *
     * @param stat The unique identifier of the stat to set.
     * @param value The new Long exp for the stat.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun set(
        stat: String,
        value: Long,
    )

    /**
     * Sets the exp of a specific stat for the managed player, converting an integer to a Long.
     * This updates the stat in memory; call [save] to persist changes.
     *
     * @param stat The unique identifier of the stat to set.
     * @param value The new integer exp for the stat, which will be converted to Long.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun set(
        stat: String,
        value: Int,
    )

    /**
     * Increases the exp of a specific stat for the managed player by a Long amount.
     * This updates the stat in memory; call [save] to persist changes.
     *
     * @param stat The unique identifier of the stat to increment.
     * @param value The Long amount to add to the current stat exp.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun increment(
        stat: String,
        value: Long,
    ): Long

    /**
     * Increases the exp of a specific stat for the managed player by an integer amount.
     * This updates the stat in memory; call [save] to persist changes.
     *
     * @param stat The unique identifier of the stat to increment.
     * @param value The integer amount to add to the current stat exp, which will be converted to Long.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun increment(
        stat: String,
        value: Int,
    ): Long

    /**
     * Decreases the exp of a specific stat for the managed player by a Long amount.
     * This updates the stat in memory; call [save] to persist changes.
     *
     * @param stat The unique identifier of the stat to decrement.
     * @param value The Long amount to subtract from the current stat exp.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun decrement(
        stat: String,
        value: Long,
    ): Long

    /**
     * Decreases the exp of a specific stat for the managed player by an integer amount.
     * This updates the stat in memory; call [save] to persist changes.
     *
     * @param stat The unique identifier of the stat to decrement.
     * @param value The integer amount to subtract from the current stat exp, which will be converted to Long.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun decrement(
        stat: String,
        value: Int,
    ): Long

    /**
     * Loads the exp of a specific stat for the managed player from persistent storage into memory.
     * If the player's data for this stat is not found, it defaults to the stat's configured random initial exp.
     *
     * @param stat The unique identifier of the stat to load.
     * @return The loaded Long exp of the stat.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun load(stat: String): Long

    /**
     * Loads the exps of all registered stats for the managed player from persistent storage into memory.
     * This typically iterates through all stats known to the global manager and calls [load] for each.
     */
    fun load()

    /**
     * Saves the current in-memory exp of a specific stat for the managed player to persistent storage.
     *
     * @param stat The unique identifier of the stat to save.
     * @throws IllegalArgumentException if the specified stat does not exist in the global stat registry.
     */
    fun save(stat: String)

    /**
     * Saves the current in-memory exps of all managed stats for the player to persistent storage.
     * This typically iterates through all currently known stat exps and calls [save] for each.
     */
    fun save()
}
