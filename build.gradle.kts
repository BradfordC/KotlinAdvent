val ktor_version = "3.4.0"

plugins {
    kotlin("jvm") version "2.3.0"
}

kotlin {
    jvmToolchain(25)
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "9.3.0"
    }
}

dependencies {
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("org.junit.jupiter:junit-jupiter:6.0.2")
}
repositories {
    mavenCentral()
}
