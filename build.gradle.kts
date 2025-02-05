plugins {
    java
    idea
}

group = "com.varlanv"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

dependencies {
    implementation("org.jetbrains:annotations:26.0.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux:3.4.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.2")
    testImplementation("io.rest-assured:spring-web-test-client:5.5.0")
    testImplementation("io.rest-assured:spring-web-test-client:5.5.0")
    testImplementation("org.json:json:20250107")
    testImplementation("io.swagger.core.v3:swagger-annotations:2.2.28")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
}

tasks.test {
    useJUnitPlatform()
    getJvmArgs()!!.add("-XX:TieredStopAtLevel=1")
    getJvmArgs()!!.add("-noverify")
}
