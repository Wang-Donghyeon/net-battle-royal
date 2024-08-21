plugins {
    kotlin("jvm") version "1.7.21"
}

val targetJavaVersion = 17
kotlin {
    jvmToolchain(targetJavaVersion)
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://papermc.io/repo/repository/maven-public/")
    }

    dependencies {
        compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

        implementation(kotlin("stdlib"))
        implementation(kotlin("reflect"))
        implementation("io.github.monun:kommand-api:3.1.7")
        implementation("io.github.monun:tap-api:4.9.8")
    }

    tasks.processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }

    /*
    tasks.withType<Jar> {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        archiveVersion.set("")

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        from(sourceSets["main"].output)

        dependsOn(configurations.runtimeClasspath)

        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
    }
     */

    tasks.register<Jar>("paperJar") {
        archiveBaseName.set(rootProject.name)
        archiveVersion.set("")
        from(sourceSets["main"].output)

        doLast {
            copy {
                from(archiveFile)
                val plugins = File(rootDir, ".paper/plugins/")
                into(if (File(plugins, archiveFileName.get()).exists()) File(plugins, "update") else plugins)
            }
        }
    }
}

group = "io.github.anblusis"
version = "1.0-SNAPSHOT"
