
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
}

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

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.core)

    implementation(libs.openapi.core)
    implementation(libs.openapi.swagger)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
