# Alphamail
A one-stop repository for all of your notification templates. For internal projects where
sendgrid cannot be used, this project aims to mimic a minimal sendgrid.

# Getting Started

## Prerequisites
1. Docker
2. Java
3. IDE (preferably Intellij)

## Setting up the development environment

1. Clone this repository 
```shell
$ git clone git@github.com:notification-forge/notif-be.git
```

2. Start the mariadb container
```shell
$ docker-compose up -d mariadb
```

3. Import the project to Intellij

4. Starting the project
   
Alphamail authenticates against an active directory. However, for the sake for development, it can make use of an in-memory credentials. 
   
To enable in-memory authentication, pass the program arguments `--spring.profiles.active=dev` before starting the project.

The project features default application parameters for connecting to the database. Please refer to the `/src/main/resources/application.resources`. 
Should you wish to override the default settings, you can either create your own application properties and pass them at start up as a program arguments
or just overriding the individual parameters. The parameter properties are as follows:

* `db.url` - The url to your database e.g. jdbc:mariadb://localhost:3306/alphamail
* `db.username` - Your database's username
* `db.password`messaging-service/src/main/kotlin/com/forge/messageservice/common/files/SimilarFilenameGenerator.kt - Your database's password

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
