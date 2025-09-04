package io.github.mulgyeolpyo.stat.loader.internal

import java.io.File
import java.lang.reflect.Modifier
import java.net.URLClassLoader
import java.util.jar.JarFile

@Suppress("unused")
internal object LibraryLoader {
    fun <T : Any> loadJarClass(
        jar: File,
        type: Class<T>,
    ): Class<out T>? {
        if (!jar.exists() || !jar.name.endsWith(".jar")) {
            return null
        }

        URLClassLoader(arrayOf(jar.toURI().toURL()), type.classLoader).use { classLoader ->
            JarFile(jar).use { jarFile ->
                return jarFile
                    .entries()
                    .asSequence()
                    .filter { !it.isDirectory && it.name.endsWith(".class") }
                    .map { it.name.replace('/', '.').removeSuffix(".class") }
                    .mapNotNull { name ->
                        runCatching {
                            classLoader
                                .loadClass(name)
                                .takeIf {
                                    type.isAssignableFrom(it) &&
                                        !Modifier.isAbstract(it.modifiers) &&
                                        !it.isInterface
                                }?.asSubclass(type)
                        }.getOrNull()
                    }.firstOrNull()
            }
        }
    }
}
