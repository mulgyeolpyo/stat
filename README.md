# Mulgyeolpyo Studio, Stat Module (only PaperMC v1.20.1)

[![Java](https://img.shields.io/badge/java-17-ED8B00.svg?logo=java)](https://www.azul.com/)
[![Kotlin](https://img.shields.io/badge/kotlin-2.1.0-585DEF.svg?logo=kotlin)](http://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/gradle-8.14.2-02303A.svg?logo=gradle)](https://gradle.org)
[![GitHub](https://img.shields.io/github/license/seorin21/paper-sample-complex)](https://www.gnu.org/licenses/gpl-3.0.html)
[![Youtube](https://img.shields.io/badge/youtube-서린-red.svg?logo=youtube)](https://www.youtube.com/@seorin._.021)

<hr>

Forked by **[seorin21/paper-sample-complex](https://github.com/seorin21/paper-sample-complex)**

[**<u>English</u>**](README.md)　|　[**<u>한국어</u>**](docs/ko-KR.md)<br>
<span style="color: #A8AEB7">_Note: The 🌏English translation is machine-generated and may contain errors._</span>

<hr>

**For content, for servers,**<br>
This is a PaperMC library that helps seamlessly manage stats, a commonly used RPG element.

## Feature
- **GlobalStatManager**: The central hub for managing all stat-related data. <br>
  <span style="color: #A8AEB7">_(It is recommended to declare and use a single instance for management.)_</span>
- **StatEventListener**: An event listener containing helper functions to simplify access to individual stats.

<span style="color: #967BDC">_While many other features are implemented, they are omitted here as you will rarely need to interact with their classes directly._</span>

The default data storage path is `/{PluginDataFolder}/stat/~`.<br>

Although creating an instance with `GlobalStatManager.create` is the standard method,<br>
you can also use `GlobalStatManager.load` to automatically register events from `event.jar` files placed within the data folder.

<span style="color: #A8AEB7">_A sample project demonstrating this approach will be added in the future._</span>

<hr>

### Gradle `stat-api`

```groovy
repositories {
    mavenCentral()
}
dependencies {
    implementation 'io.github.mulgyeolpyo:stat-api:<version>'
}
```

### Maven `stat-api`
```xml
<repositories>
    <repository>
        <id>central</id>
        <url>https://repo.maven.apache.org/maven2</url>
    </repository>
</repositories>
<dependencies>
<dependency>
    <groupId>io.github.mulgyeolpyo</groupId>
    <artifactId>stat-api</artifactId>
    <version><version></version></version>
</dependency>
</dependencies>
```

### WARNING ⚠
<span style="color: #ED5466">When using this module, you must add the following to your `plugin.yml`.</span>
```yaml
libraries:
  - io.github.mulgyeolpyo:stat-api:<version>
```

<hr>

## Example
### 1. Create a Manager For Stats
```kotlin
val globalStatManager = GlobalStatManager.create(plugin = this) // The plugin instance must be passed to `this`.
// or 
val globalStatManager = GlobalStatManager.create() // In this case, it automatically finds the plugin instance.
```

### 2. Add a Stat
```kotlin
/* Choose one of the methods below. */
val stat = globalStatManager.register(stat = statName)
/* 
    For the `event` parameter, create a class that references StatEventListener.
    (StatEventListener is used for convenient stat access)
    
    Ex) stat(player).increment(1) // Refer to the StatEventListener.kt file in the 'stat-plugin' folder.
*/
val stat = globalStatManager.register(stat = statName, event = StatEventListener::class.java)
val stat = globalStatManager.register(event = StatEventListener::class.java)
```

### 3. Access a Stat
```kotlin
/*
    For intuitive and easy stat access, you can use StatEventListener.
    However, if you want to access stats directly, use the method below.
 */
val playerStats = globalStatManager.create(player = player)
val strengthStat = playerStats.getStat(stat = "strength") // Gets the stat named "strength".

// StatEventListener includes code that simplifies this process.
```

### 4. Access Stat Configuration
```kotlin
/*
    Stat configuration can be accessed through the GlobalStatManager.
 */
val statConfig = globalStatManager.getStatConfig(stat = "strength") // Gets the configuration for the stat named "strength".

// The feature to modify the stat elements themselves is not yet implemented.
// For intuitive modification of stat settings, check the default configuration path at '/{pluginDataFolder}/stat/~'.
```
