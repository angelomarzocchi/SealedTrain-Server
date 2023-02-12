

val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val commonsCodecVersion: String by project
val kmongoVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.7.20"
    id("io.ktor.plugin") version "2.1.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.20"

}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}



repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-core-jvm:2.2.2")
    implementation("io.ktor:ktor-server-auth-jvm:2.2.2")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.2.2")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.2.2")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.2.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.2.2")
    implementation("io.ktor:ktor-server-netty-jvm:2.2.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

    implementation("commons-codec:commons-codec:$commonsCodecVersion")
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongoVersion")
    testImplementation("io.ktor:ktor-server-tests-jvm:2.2.2")

}