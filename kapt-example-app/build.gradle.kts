plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("kapt") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    application
}

application {
    mainClass.set("kaptexample.app.MainKt")
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":kapt-example-core"))
    implementation(project(":kapt-example-processor"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    kapt(project(":kapt-example-processor"))
}

repositories {
    mavenCentral()
}
