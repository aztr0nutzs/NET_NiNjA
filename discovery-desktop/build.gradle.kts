plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  jvm("desktop")
  sourceSets {
    val desktopMain by getting {
      dependencies {
        implementation(project(":core-model"))
        implementation(project(":core-engine"))
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.serialization.json)
      }
    }
    val desktopTest by getting {
      dependencies { implementation(kotlin("test")) }
    }
  }
}
