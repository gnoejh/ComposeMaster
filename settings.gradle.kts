pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Compose Master"
include(":app")
include(":composelayouts")
include(":advancedlayouts")
include(":animations")
include(":imageprocessing")
include(":videoprocessing")
include(":state")
include(":sideeffects")
include(":lifecycle")
include(":mltflow")
