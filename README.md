# testTheRest

A test task for testing REST service 'jsonplaceholder.typicode.com'

## Used tools / libraries

- Java 21.0.1
- Gradle 8.8
- Google checkstyle
- Lombok
- RestAssured
- TestNG
- IntelliJ IDEA as an IDE
- GitHub (see the repository [here](https://github.com/pskrebnev/testTheRest))

## How to run

There are following types of tests: 'delete', 'get', 'patch', 'post', 'put', 'critical path', '
negative'.
In order to run particular type of tests, you need to specify it in the command line like this:
`./gradlew test -PgrouptoBuild=negative` where 'negative' can be replaced with any of the types
mentioned above.

## Test results
See reports in the `build/testng-reports/index.html`.
You can open it by IntelliJ IDEA 'open in' -> 'browser' -> <any browser you like>.
You can also see the results in the console.

## Future improvements
[TBD]
