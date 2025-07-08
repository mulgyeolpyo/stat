plugins {
    alias(libs.plugins.publish)
    signing
}

publishing {
    repositories {
        mavenLocal()

        maven {
            name = "server"
            url = rootProject.uri(".server/libraries")
        }
    }

    publications {
        fun MavenPublication.setup(target: Project) {
            artifactId = target.name
            from(target.components["java"])
            artifact(target.tasks["sourcesJar"])
            artifact(target.tasks["dokkaJar"])
        }

        create<MavenPublication>("api") {
            setup(projectApi)
        }

        create<MavenPublication>("core") {
            setup(projectCore)
            artifact(coreReobfJar)
        }
    }
}

mavenPublishing {
    pom {
        name.set(rootProject.name)
        description.set("유튜브 \"서린\"의 컨텐츠 서버 '잔혹한 추위'를 위한, 의존성 라이브러리입니다.")
        url.set("https://github.com/mulgyeolpyo/${rootProject.name}")

        licenses {
            license {
                name.set("GNU General Public License version 3")
                url.set("https://opensource.org/licenses/GPL-3.0")
            }
        }

        developers {
            developer {
                id.set("seorin21")
                name.set("서린")
                email.set("seorin021@gmail.com")
                url.set("https://github.com/seorin21")
                roles.addAll("developer")
                timezone.set("Asia/Seoul")
            }
        }

        scm {
            connection.set("scm:git:git://github.com/mulgyeolpyo/${rootProject.name}.git")
            developerConnection.set("scm:git:ssh://github.com:mulgyeolpyo/${rootProject.name}.git")
            url.set("https://github.com/mulgyeolpyo/${rootProject.name}")
        }
    }

    publishToMavenCentral()
}

signing {
    isRequired = true
    sign(publishing.publications)
}

projectPlugin.tasks.named("clipJar") {
    dependsOn(tasks.named("publishApiPublicationToServerRepository"))
    dependsOn(tasks.named("publishCorePublicationToServerRepository"))
}

tasks.register("publishMaven")
tasks.named("publishMaven") {
    dependsOn(tasks.named("publishApiPublicationToMavenCentralRepository"))
    dependsOn(tasks.named("publishCorePublicationToMavenCentralRepository"))
}
