import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.application)
  alias(libs.plugins.compose)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  androidTarget()
  jvm("desktop")

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":core-model"))
        implementation(project(":core-engine"))
        implementation(project(":storage"))

        // MVP: providers included directly. Later: expect/actual or DI selection.
        implementation(project(":discovery-android"))
        implementation(project(":discovery-desktop"))

        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.serialization.json)

        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material3)
      }
    }
    val androidMain by getting {
      dependencies {
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.lifecycle.runtime.compose)
      }
    }
    val desktopMain by getting {
      dependencies { implementation(compose.desktop.currentOs) }
    }
  }
}

android {
  namespace = "com.example.netadmin.app"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.example.netadmin"
    minSdk = 24
    targetSdk = 35
    versionCode = 1
    versionName = "0.1.0"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  packaging {
    resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" }
  }
}

compose.desktop {
  application {
    mainClass = "com.example.netadmin.app.desktop.DesktopMainKt"
    nativeDistributions {
      targetFormats(TargetFormat.Msi, TargetFormat.Deb)
      packageName = "NetDiscoveryAdmin"
      packageVersion = "0.1.0"
    }
  }
}
