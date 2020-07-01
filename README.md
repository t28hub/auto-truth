# AutoTruth
[![GitHub Actions](https://github.com/t28hub/auto-truth/workflows/Auto%20Truth/badge.svg)](https://github.com/t28hub/auto-truth/actions)
[![FOSSA Status](https://app.fossa.io/api/projects/custom%2B14538%2Fauto-truth.svg?type=shield)](https://app.fossa.io/projects/custom%2B14538%2Fauto-truth?ref=badge_shield)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=io.t28.auto.truth&metric=alert_status)](https://sonarcloud.io/dashboard?id=io.t28.auto.truth)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=io.t28.auto.truth&metric=coverage)](https://sonarcloud.io/dashboard?id=io.t28.auto.truth)
 
Generate the [Truth](https://truth.dev/) extensions for your value classes  using annotation processor.

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
