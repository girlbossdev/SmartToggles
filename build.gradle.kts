plugins {
    id("net.fabricmc.fabric-loom") version "1.17-SNAPSHOT"
}

group = "dev.girlboss"
version = "1.0.0" + "+${sc.current.version}"

base {
    archivesName = rootProject.name
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net")
    maven("https://maven.terraformersmc.com/")
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")

    implementation("net.fabricmc:fabric-loader:0.19.3")
    implementation("net.fabricmc.fabric-api:fabric-api:${property("mods.fabric_api")}")
    implementation("com.terraformersmc:modmenu:${property("mods.modmenu")}")
}

val javaVersion = JavaVersion.VERSION_25

java {
    toolchain {
        vendor = JvmVendorSpec.MICROSOFT
        languageVersion = JavaLanguageVersion.of(javaVersion.majorVersion)
    }
}

tasks.processResources {
    val properties = mapOf(
        "version" to version,
        "java" to ">=${javaVersion.majorVersion}",
        "minecraft" to ">=${sc.current.version}",
    )

    inputs.properties(properties)

    filesMatching("fabric.mod.json") {
        expand(properties)
    }

    filesMatching("*.mixins.json") {
        expand("java" to javaVersion.majorVersion)
    }
}
