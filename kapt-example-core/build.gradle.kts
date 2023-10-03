plugins {
    kotlin("jvm") version "1.7.20"
}

dependencies {
    implementation(kotlin("stdlib"))
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.7.20")
}

repositories {
    jcenter()
}
