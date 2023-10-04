plugins {
    kotlin("jvm") version "1.7.20"
}

dependencies {
    implementation(kotlin("stdlib"))
//    implementation("com.sun.java:tools:11.0.1")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.7.20")
}

repositories {
    jcenter()
    mavenCentral()
}
