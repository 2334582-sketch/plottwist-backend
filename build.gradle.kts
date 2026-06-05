plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
    application
}

group = "com.plottwist.backend"
version = "0.0.1"

application {
    mainClass.set("com.plottwist.backend.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    // Librerías oficiales de Ktor 2.3.8 que SÍ existen en Maven
    implementation("io.ktor:ktor-server-core-jvm:2.3.8")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.8")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.8")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.8")
    implementation("ch.qos.logback:logback-classic:1.4.14")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.plottwist.backend.ApplicationKt"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}