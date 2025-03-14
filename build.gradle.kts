import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.detekt)
}

private val javaVersion = "17"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

// Скачает ли оно само жаву, если таковой не будет?
private val javaToolchainService = project.extensions.getByType<JavaToolchainService>()
private val jdkPath = javaToolchainService.launcherFor {
    languageVersion.set(JavaLanguageVersion.of(javaVersion.toInt()))
}.get().metadata.installationPath.asFile.absolutePath

group = "com.example"
version = "0.0.1"

application {
    mainClass = "com.example.ApplicationKt"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

/** (Workaround for "cannot serialize object of type 'java.io.PipedInputStream'")[https://github.com/gradle/gradle/issues/21364]
 */
tasks.withType<JavaExec>().configureEach {
    if (name.endsWith("main()")) {
        notCompatibleWithConfigurationCache("JavaExec created by IntelliJ")
    }
}

detekt {
    toolVersion = libs.versions.detekt.get()
    allRules = true
    ignoreFailures = true
}

tasks.withType<Detekt>().configureEach {
    this.jvmTarget = javaVersion
    jdkHome.set(file(jdkPath))

    reports {
        html {
            required.set(true)
            outputLocation.set(file("${project.rootDir}/detekt/reports/detektReport.html"))
        }
        xml.required.set(false)
        md.required.set(false)
        txt.required.set(false)
        sarif.required.set(false)
    }
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.core)

    implementation(libs.openapi.core)
    implementation(libs.openapi.swagger)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)

    detektPlugins(libs.detekt.formatting)
}
