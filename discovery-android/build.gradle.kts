plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.android.library)
}

kotlin {
  androidTarget()
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":core-model"))
        implementation(project(":core-engine"))
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.serialization.json)
      }
    }
  }
}

android {
  namespace = "com.example.netadmin.discovery.android"
  compileSdk = 35
  defaultConfig { minSdk = 24 }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}
