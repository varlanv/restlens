plugins {
    `java-gradle-plugin`
}

val isCiBuild = providers.environmentVariable("CI").orNull != null

if (isCiBuild) {
    java {
        version = JavaVersion.VERSION_17
    }
} else {
    java {
        toolchain {
            vendor.set(JvmVendorSpec.AZUL)
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

repositories {
    if (!isCiBuild) {
        mavenLocal()
    }
    mavenCentral()
}

dependencies {
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.lombok)
    compileOnly(libs.junit.platform.launcher)
    annotationProcessor(libs.lombok)
}

gradlePlugin {
    plugins {
        create("internalGradleConventionPlugin") {
            id = libs.plugins.internalConvention.get().pluginId
            implementationClass = "org.restlens.gradle.plugin.InternalConventionPlugin"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
