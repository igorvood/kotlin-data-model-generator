plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("kapt") version "1.7.20"
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":kapt-example-core"))
    implementation("com.google.auto.service:auto-service:1.0.1")
//    implementation ("com.squareup:kotlinpoet:0.5.0")
    implementation ("com.squareup:kotlinpoet:1.14.2")
//    implementation("io.arrow-kt:arrow-core:1.2.1")

    kapt("com.google.auto.service:auto-service:1.0.1")
}

repositories {
    mavenCentral()
    google()
}
