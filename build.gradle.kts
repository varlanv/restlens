plugins {
    java
    alias(libs.plugins.internalConvention)
}

dependencies {
    implementation("org.jetbrains:annotations:26.0.2")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux:3.4.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.2")
    testImplementation("io.rest-assured:spring-web-test-client:5.5.0")
    testImplementation("org.json:json:20250107")
    testImplementation("io.swagger.core.v3:swagger-annotations:2.2.28")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
}
