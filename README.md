# Multi-module Kotlin Project with Gradle
This application demonstrates how to structure a multi-module kotlin project with gradle.

This is also a proof-of-concept to demonstrate an application's capability to load 
extensions (or plugins) at runtime.

## Module Dependency

![Module Dependency](docs/mm-kt-gdl.png?raw=true "Module Dependency")

## Directory Structure

[buildSrc/build.gradle.kts](buildSrc/build.gradle.kts) - Enables the precompiled script plugins.
See [Precompiled Plugins](https://docs.gradle.org/current/userguide/custom_plugins.html#sec:precompiled_plugins)

[buildSrc/src/main/kotlin/rt.gradle.kts](buildSrc/src/main/kotlin/rt.gradle.kts) - Contains the common project dependencies e.g. repositories
dependencies. ```rt``` is the id of the ```precompiled script plugin```

The ```rt``` plugin is consumed by other modules as:
```kotlin
plugins { 
    ...
    id("rt")
}
```

[shared](shared) - Contains the classes that are common to both [lib](lib) and [messaging-service](messaging-service)

[lib](lib) - The standalone jar that will be loaded by [messaging-service](messaging-service) at runtime.

[messaging-service](messaging-service) - The application's main interface.

## Declaring the multi-module gradle project
[settings.gradle.kts](settings.gradle.kts)
```kotlin
rootProject.name = "multi-module-kt-gradle"
include("app", "lib", "shared")
```

## Git Branching Strategy

### `feature`, `bug`, `chore`, `refactor` and `hot-fix`

These branches are short-lived. Below are the descriptions of the branch types:

1. `feature`: Contain code for any new feature
2. `bug`: Fixes for any bug discovered
3. `chore`: Work that will not affect end user (e.g updating `.gitignore`)
4. `refactor`: Refactoring of code
5. `hot-fix`: Urgent fix for a deployed version

Branches should be named: `type/description` (e.g `feature/login`)

| Branch Type | Branch From   |
| ----------- | ------------- |
| `feature`   | `main` |
| `bug`       | `main` |
| `chore`     | `main` |
| `refactor`  | `main` |
| `hot-fix`   | `main`      |

### Versioning

A version will typically look like this `MAJOR.MINOR.REVISION`

- A `MAJOR` version increment signifies a MAJOR version release. Perhaps with lots of new features added
- A `MINOR` version increment signifies a MINOR version release. Perhaps a few minor features and bug fixes
- A `REVISION` version increment signifies a deployment with hot-fixes
