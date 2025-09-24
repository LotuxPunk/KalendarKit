import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "be.vandeas"
version = "1.1.0"

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(libs.kotlinx.datetime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

android {
    namespace = "be.vandeas.kalendar.kit"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "kalendar-kit", version.toString())

    pom {
        name = "KalendarKit"
        description = """
            KalendarKit is a Compose Multiplatform library designed to simplify the presentation of events for users to add. Using native APIs, KalendarKit allows developers to present a modal EventKit interface on 
            iOS and open the default calendar application on Android, making it easier to add events.
        """.trimIndent()
        inceptionYear = "2025"
        url = "https://github.com/LotuxPunk/KalendarKit/"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "lotuxpunk"
                name = "Clément VANDENDAELEN"
                url = "https://vandeas.be"
            }
        }
        scm {
            url = "https://github.com/LotuxPunk/KalendarKit/"
            connection = "scm:git:git://github.com/LotuxPunk/KalendarKit.git"
            developerConnection = "scm:git:ssh://git@github.com:LotuxPunk/KalendarKit.git"
        }
    }
}
