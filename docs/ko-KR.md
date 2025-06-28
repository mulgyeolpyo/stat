# 물결표 스튜디오, 스탯 모듈 (only PaperMC v1.20.1)

[![Java](https://img.shields.io/badge/java-17-ED8B00.svg?logo=java)](https://www.azul.com/)
[![Kotlin](https://img.shields.io/badge/kotlin-2.1.0-585DEF.svg?logo=kotlin)](http://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/gradle-8.2.1-02303A.svg?logo=gradle)](https://gradle.org)
[![GitHub](https://img.shields.io/github/license/seorin21/paper-sample-complex)](https://www.gnu.org/licenses/gpl-3.0.html)
[![Youtube](https://img.shields.io/badge/youtube-서린-red.svg?logo=youtube)](https://www.youtube.com/@seorin._.021)

<hr>

[**<u>English</u>**](../README.md)　|　[**<u>한국어</u>**](ko-KR.md) <br>
_모든 🌏영문 번역은 AI로 진행되어 부정확한 부분이 존재할 수 있습니다._



<hr>

**컨텐츠를 위한, 서버를 위한,**<br>
흔히 사용되는 RPG 요소인 스탯 관리를 원활히 도와주는 PaperMC 관련 라이브러리입니다.

### Gradle
```groovy
repositories {
    mavenCentral()
}
dependencies {
    implementation 'io.github.mulgyeolpyo:stat-api:<version>'
}
```

### Maven
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

## 주의 ⚠
해당 모듈을 사용할 경우, `plugin.yml`에 반드시 다음을 추가해야 합니다.
```yaml
libraries:
  - io.github.mulgyeolpyo:stat-api:<version>
```

## 사용 방법
### 1. 글로벌 스탯 매니저 생성
```kotlin
val globalStatManager = GlobalStatManager.create(plugin = this) // this에 플러그인 인스턴스가 들어가야 합니다.
// or 
val globalStatManager = GlobalStatManager.create() // 이 경우, 자동으로 플러그인 인스턴스를 찾습니다.
```

### 2. 스탯 추가
```kotlin
/* 아래 방식 중 하나를 선택하시면 됩니다. */
val stat = globalStatManager.register(stat = statName)
/* 
    event 매개변수의 경우, StatEventListener를 참조하는 클래스를 생성하시면 됩니다.  
    (StatEventListener를 사용하는 이유는 간편한 스탯 접근을 위함입니다)
    
    Ex) stat(player).increment(1) // 'stat-plugin' 폴더의 StatEventListener.kt 파일을 참고하세요.
*/
val stat = globalStatManager.register(stat = statName, event = StatEventListener::class.java)
val stat = globalStatManager.register(event = StatEventListener::class.java)
```

### 3. 스탯 접근
```kotlin
/*
    직관적이고 간편한 스탯 접근은 StatEventListener를 사용하시면 되지만,
    직접적으로 스탯을 접근하고 싶다면 아래와 같이 사용하시면 됩니다.
 */
val playerStats = globalStatManager.create(player = player)
val strengthStat = playerStats.getStat(stat = "strength") // "strength"라는 이름의 스탯을 가져옵니다.

// 이 작업을 간편히 하는 코드가 포함된 것이, StatEventListener입니다.
```

### 4. 스탯 설정 접근
```kotlin
/*
    스탯 설정은 GlobalStatManager를 통한 접근이 가능합니다.
 */
val statConfig = globalStatManager.getStatConfig(stat = "strength") // "strength"라는 이름의 스탯 설정을 가져옵니다.

// 아직 스탯의 요소 자체를 변경하는 기능은 미구현되어 있습니다.
// 직관적인 스탯 설정 수정을 원하신다면, 기본 설정값인 '/{pluginDataFolder}/stat/~'를 확인해보세요/
```