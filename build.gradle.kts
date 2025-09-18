plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)
}

group = "net.azisaba.lgwloadbalancer"
version = System.getenv("VERSION") ?: "0.1.0-beta.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://repo.aikar.co/content/groups/aikar/") {
        name = "aikar-repo"
    }
    maven("https://repo.codemc.org/repository/maven-public/") {
        name = "codemc-repo"
    }
}

dependencies {
    compileOnly(libs.paper.api)
    implementation(libs.kotlin.stdlib.jdk8)

    compileOnly(libs.command.api)

    implementation(libs.kaml)
}

val targetJavaVersion = 17
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

// === For ACF-Paper===
tasks.compileJava {
    // on both
    options.compilerArgs.add("-parameters")
}

tasks.compileKotlin {
    // on kotlin
    compilerOptions.javaParameters = true
}

tasks.shadowJar {
    relocate("co.aikar.commands", "net.azisaba.lgwloadbalancer.shadow.acf")
    relocate("co.aikar.locales", "net.azisaba.lgeloadbalancer.shadow.locales")
}
// ====================
