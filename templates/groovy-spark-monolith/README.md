# Serverless [Groovy](https://groovy-lang.org/) Application with [Spark](http://sparkjava.com/)

#### Prerequisites

1) Install **Java 8**, **Groovy** and [Gradle](https://gradle.org/) - I highly recommend using [SDKMAN](http://sdkman.io/) to install any JVM-related packages.
2) Install [NodeJS](https://nodejs.org/en/) and the [Serverless Framework](https://serverless.com/framework/docs/getting-started/).
3) [Amazon Web Services](https://aws.amazon.com/) account.

You also need to set up your AWS credentials/profiles in the `~/.aws/credentials` file.

### Build

To build the project, run `npm i && ./gradlew clean build`. 

### Run Locally

```
./gradlew run
```

You can access the API at `http://localhost:4567/ping`

### Deploy

To deploy, simply run `sls deploy`.
