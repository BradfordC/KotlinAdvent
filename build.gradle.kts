val ktor_version = "2.3.7"

plugins {
    kotlin("jvm") version "1.8.21"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}

dependencies {
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("org.junit.jupiter:junit-jupiter:5.10.0")
}
repositories {
    mavenCentral()
}
