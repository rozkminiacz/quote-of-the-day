import com.google.cloud.tools.gradle.appengine.appyaml.AppEngineAppYamlExtension
import org.gradle.initialization.Environment

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.google.cloud.appengine)
    application
}


configure<AppEngineAppYamlExtension> {
    stage {
        setArtifact("build/libs/${project.name}-all.jar")
    }
    deploy {
        version = "1"
        projectId = "quotes-408216"
        version = "1"
    }
}

group = "tech.michalik.quotes"
version = "1.0.0"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.jvm)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
}

fun loadEnv() {


    file("$rootDir/.env").readLines().forEach {
        val (key, value) = it.split('=')
        System.setProperty(key, value)
    }
//    commandLine 'fooScript.sh'
}

loadEnv()