package io.github.mulgyeolpyo.stat.loader

import org.bukkit.Bukkit
import java.lang.reflect.InvocationTargetException

@Suppress("unused")
internal object LibraryLoader {
    private fun <T> loadClass(
        className: String,
        type: Class<T>,
        vararg initArgs: Any?,
    ): T {
        val parameterTypes = initArgs.map { it?.javaClass }.toTypedArray()

        return try {
            val clazz = Class.forName(className, true, type.classLoader).asSubclass(type)
            val constructor = clazz.getConstructor(*parameterTypes)
            constructor.newInstance(*initArgs)
        } catch (e: ClassNotFoundException) {
            throw UnsupportedOperationException("${type.name} does not have implement", e)
        } catch (e: IllegalAccessException) {
            throw UnsupportedOperationException("${type.name} constructor is not visible", e)
        } catch (e: InstantiationException) {
            throw UnsupportedOperationException("${type.name} is abstract class", e)
        } catch (e: InvocationTargetException) {
            throw UnsupportedOperationException("${type.name} has an error occurred while creating the instance", e)
        }
    }

    fun <T> loadImplement(
        type: Class<T>,
        vararg initArgs: Any?,
    ): T {
        val packageName = type.`package`.name
        val className = "$packageName.internal.${type.simpleName}Impl"
        return loadClass(className, type, *initArgs)
    }

    fun <T> loadNMS(
        type: Class<T>,
        vararg initArgs: Any?,
    ): T {
        val packageName = type.`package`.name
        val className = "NMS${type.simpleName}"

        val candidates =
            listOf(
                "$packageName.$libraryVersion.$className",
                "${packageName.substringBeforeLast('.', "")}.$libraryVersion.${packageName.substringAfterLast('.') }.$className",
            )

        for (candidate in candidates) {
            runCatching {
                return loadClass(candidate, type, *initArgs)
            }
        }

        throw UnsupportedOperationException("${type.name} does not support this version: $libraryVersion")
    }

    private val minecraftVersion by lazy { Bukkit.getServer().minecraftVersion }
    private val libraryVersion by lazy { "v${minecraftVersion.replace('.', '_')}" }
}
