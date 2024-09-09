plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm") version "1.9.20-RC2"
    id("io.papermc.paperweight.userdev") version "1.7.2"
    id("maven-publish")
}

val targetJavaVersion = 21
val pluginGroupId = "com.github.fhanko"
val pluginVersion = "2.0"
allprojects {
    apply(plugin = "java-library")
    apply(plugin = "io.papermc.paperweight.userdev")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")

    repositories {
        maven("https://repo.jeff-media.com/public")
    }

    dependencies {
        paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    }
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
}

subprojects {
    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                groupId = pluginGroupId
                version = pluginVersion

                afterEvaluate {
                    artifactId = tasks.jar.get().archiveBaseName.get()
                }
            }
        }
    }
}

