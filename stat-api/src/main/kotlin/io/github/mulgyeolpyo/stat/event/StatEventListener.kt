package io.github.mulgyeolpyo.stat.event

import io.github.mulgyeolpyo.stat.GlobalStatManager
import io.github.mulgyeolpyo.stat.StatManager
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.util.UUID

/**
 * Base class for stat-related event listeners.
 *
 * Subclasses of `StatEventListener` are intended to listen for events that involve a specific stat.
 *
 * If the `stat` parameter is not explicitly provided in the constructor, the stat name will be
 * automatically derived from the subclass's simple class name by removing the "StatEvent" suffix
 * and converting it to lowercase. For example, a subclass named `StrengthStatEvent` would default
 * to managing the "strength" stat.
 *
 * Implementations should ensure their class names follow the `{StatName}StatEvent` convention if
 * they rely on automatic stat name derivation.
 *
 * @property stat The name of the stat this listener is associated with. If null, it's derived from the class name.
 * @property manager The [GlobalStatManager] instance used to access and manipulate player stats.
 */
@Suppress("unused")
open class StatEventListener(
    stat: String? = null,
    val manager: GlobalStatManager,
) : Listener {
    private val stat: String =
        stat ?: this@StatEventListener::class.java.simpleName
            .removeSuffix("StatEvent")
            .lowercase()

    /**
     * Provides an [InnerStatManager] instance for a specific player, allowing manipulation of the stat
     * associated with this listener.
     *
     * @param player The [Player] whose stat is to be managed.
     * @return An [InnerStatManager] instance bound to the player's stat.
     */
    protected fun stat(player: Player): InnerStatManager = InnerStatManager(manager.create(player.uniqueId))

    /**
     * Provides an [InnerStatManager] instance for a specific player (identified by UUID),
     * allowing manipulation of the stat associated with this listener.
     *
     * @param playerId The [UUID] of the player whose stat is to be managed.
     * @return An [InnerStatManager] instance bound to the player's stat.
     */
    protected fun stat(playerId: UUID): InnerStatManager = InnerStatManager(manager.create(playerId))

    /**
     * An inner class that provides convenient methods to interact with the specific stat
     * managed by the outer [StatEventListener] for a particular player.
     *
     * @property manager The [StatManager] instance for a specific player.
     */
    inner class InnerStatManager(
        private val manager: StatManager,
    ) {
        /**
         * Retrieves the current value of the stat associated with the outer [StatEventListener].
         * @return The current float value of the stat.
         */
        fun get(): Float = manager.get(this@StatEventListener.stat)

        /**
         * Sets the value of the stat associated with the outer [StatEventListener].
         * @param value The new float value to set.
         */
        fun set(value: Float) = manager.set(this@StatEventListener.stat, value)

        /**
         * Sets the value of the stat associated with the outer [StatEventListener].
         * @param value The new integer value to set.
         */
        fun set(value: Int) = manager.set(this@StatEventListener.stat, value)

        /**
         * Increments the value of the stat associated with the outer [StatEventListener] by a float amount.
         * @param value The float amount to increment by.
         */
        fun increment(value: Float) = manager.increment(this@StatEventListener.stat, value)

        /**
         * Increments the value of the stat associated with the outer [StatEventListener] by an integer amount.
         * @param value The integer amount to increment by.
         */
        fun increment(value: Int) = manager.increment(this@StatEventListener.stat, value)

        /**
         * Decrements the value of the stat associated with the outer [StatEventListener] by a float amount.
         * @param value The float amount to decrement by.
         */
        fun decrement(value: Float) = manager.decrement(this@StatEventListener.stat, value)

        /**
         * Decrements the value of the stat associated with the outer [StatEventListener] by an integer amount.
         * @param value The integer amount to decrement by.
         */
        fun decrement(value: Int) = manager.decrement(this@StatEventListener.stat, value)

        /**
         * Saves the current value of the stat associated with the outer [StatEventListener] to persistent storage.
         */
        fun save() = manager.save(this@StatEventListener.stat)

        /**
         * Loads the value of the stat associated with the outer [StatEventListener] from persistent storage.
         */
        fun load() = manager.load(this@StatEventListener.stat)
    }
}
