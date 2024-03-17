import org.gradle.kotlin.dsl.accessors.runtime.externalModuleDependencyFor

plugins {
    idea
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "de.hype.bbsentials.profileidfromlogs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.hypixel.net/repository/Hypixel/")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation(files("lib/intellij_forms_rt.jar"))
    implementation("net.hypixel:hypixel-api-transport-reactor:4.4")
}

tasks.test {
    useJUnitPlatform()
}
