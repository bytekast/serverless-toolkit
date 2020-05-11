# Simple Serverless [Groovy](https://groovy-lang.org/) Application

#### Prerequisites

1) Install **Java 8** and **Groovy** - I highly recommend using [SDKMAN](http://sdkman.io/) to install any JVM-related packages.
2) Install [NodeJS](https://nodejs.org/en/) and the [Serverless Framework](https://serverless.com/framework/docs/getting-started/).
3) [Amazon Web Services](https://aws.amazon.com/) account.

You also need to set up your AWS credentials/profiles in the `~/.aws/credentials` file.

### Run Locally

To run the service locally: `npm i && groovy Routes`.

You can connect to the local API via `http://localhost:4567/hello`

### Build

To download the dependencies and prepare the deployment package, run: `npm i && sls package`

### Deploy

To deploy, simply run `sls deploy`.

To invoke the function, run:

```
sls invoke -f hello
```