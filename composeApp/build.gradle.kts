import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            val exposedVersion = "0.55.0"
            implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
            implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
            implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
            implementation("org.xerial:sqlite-jdbc:3.46.1.0")
            implementation("ch.qos.logback:logback-classic:1.5.6")
        }

        jvmTest.dependencies {
            implementation(kotlin("test"))
            implementation("org.junit.jupiter:junit-jupiter:5.10.2")
        }
    }
}






compose.desktop {
    application {
        mainClass = "com.ticketmachine.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.ticketmachine"
            packageVersion = "1.0.0"
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
}