// An init script to override a build configuration to use a snapshot version of NullAway
allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
    }
}

gradle.projectsLoaded {
    rootProject.allprojects {
        configurations.all {
            resolutionStrategy {
                eachDependency {
                    if (requested.group == "org.assertj" && requested.name == "assertj-bom") {
                        useVersion("+")
                    }
                }
                cacheChangingModulesFor(0, "seconds")
                cacheDynamicVersionsFor(0, "seconds")
            }
        }
    }
}
