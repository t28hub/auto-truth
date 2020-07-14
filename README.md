# AutoTruth
[![GitHub Actions](https://github.com/t28hub/auto-truth/workflows/Auto%20Truth/badge.svg)](https://github.com/t28hub/auto-truth/actions)
[![FOSSA Status](https://app.fossa.io/api/projects/custom%2B14538%2Fauto-truth.svg?type=shield)](https://app.fossa.io/projects/custom%2B14538%2Fauto-truth?ref=badge_shield)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=io.t28.auto.truth&metric=alert_status)](https://sonarcloud.io/dashboard?id=io.t28.auto.truth)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=io.t28.auto.truth&metric=coverage)](https://sonarcloud.io/dashboard?id=io.t28.auto.truth)
 
Generate the [Truth](https://truth.dev/) extensions for value objects for Java 8+.

## Usage
Add the `@AutoTruth` annotation to a custom subject class and specify a value object class.  
This example uses [Employee.java](https://github.com/google/truth/blob/master/core/src/test/java/com/google/common/truth/extension/Employee.java).
```java
@AutoSubject(Employee.class)
class EmployeeSubject {
}
```
Then AutoTruth generates the `AutoEmployeeSubject` that is declared the following methods:
* `hasUsername(String)`
* `hasId(long)`
* `hasName(String)`
* `hasLocation(Location)`
* `isCeo()`
* `isNotCeo()`

You can extends the `AutoEmployeeSubject` as follows, if the generated methods are not enough.
```java
@AutoSubject(Employee.class)
public class EmployeeSubject extends AutoEmployeeSubject {
    private final Employee actual;

    EmployeeSubject(FailureMetadata failureMetadata, Employee actual) {
        super(failureMetadata, actual);
        this.actual = actual;
    }

    public static EmployeeSubject assertThat(Employee actual) {
        return Truth.assertAbout(EmployeeSubject::new).that(actual);
    }
    
    public LocationSubject location() {
        final Location actual = this.actual.location();
        return check("location()").about(LocationSubject::new).that(actual);
    }
    
    @AutoSubject(Employee.Location.class)
    public static class LocationSubject {}
}
```

### Supported types
AutoTruth supports the following types:
#### Primitive types
* `boolean`
* `byte`
* `char`
* `short`
* `int`
* `long`
* `float`
* `double`

#### Array types
* `boolean[]`
* `byte[]`
* `char[]`
* `short[]`
* `int[]`
* `long[]`
* `float[]`
* `double[]`
* `Object[]`

#### Java8 types
* `Optional`
* `OptionalInt`
* `OptionalLong`
* `OptionalDouble`
* `Stream`
* `IntStream`
* `LongStream`

#### Other JDK types
* `Enum`
* `Object`
* `Class`
* `String`
* `Iterable`
* `Map`
  
## Installing
The AutoTruth packages are available on the [GitHub Packages](https://github.com/t28hub/auto-truth/packages).  
You need an access token to install packages in GitHub Packages.   
Instructions for creating an access token is described in [GitHub Help](https://help.github.com/en/packages/publishing-and-managing-packages/about-github-packages#about-tokens).  

### Gradle
```
repositories {
    mavenCentral()
    jcenter()
    maven {
        url 'https://maven.pkg.github.com/t28hub/auto-truth'
        credentials {
            username = 'YOUR_GITHUB_USERNAME'
            password = 'YOUR_GITHUB_TOKEN'
        }
    }
}

dependencies {
    testImplementation "com.google.truth:truth:$LATEST_TRUTH_VERSION"
    testImplementation "com.google.truth.extensions:truth-java8-extension:$LATEST_TRUTH_VERSION"
    testImplementation "io.t28.auto:auto-truth-annotations:$LATEST_VERSION"
    testAnnotationProcessor "io.t28.auto:auto-truth-processor:$LATEST_VERSION"
}
```

### Maven
See [GitHub Help](https://help.github.com/en/packages/using-github-packages-with-your-projects-ecosystem/configuring-apache-maven-for-use-with-github-packages).

## License
[![FOSSA Status](https://app.fossa.io/api/projects/custom%2B14538%2Fauto-truth.svg?type=large)](https://app.fossa.io/projects/custom%2B14538%2Fauto-truth?ref=badge_large)
