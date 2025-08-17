pluginManagement {
    repositories {

        maven("https://maven.aliyun.com/repository/public/")
        maven("https://maven.aliyun.com/repository/central/")
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

rootProject.name = "MiaoLib"
include(":miaoLibrary")
include(":app")
