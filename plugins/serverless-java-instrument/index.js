'use strict';

const {spawnSync} = require("child_process");

class JavaInstrumentPlugin {
  constructor(serverless, options) {
    this.serverless = serverless;
    this.options = options;

    this.instrumenter = `${__dirname}/lib/instrumenter.jar`
    this.aspects = `${__dirname}/lib/aspects.jar`
    this.runtime = `${__dirname}/lib/runtime.jar`

    this.hooks = {
      "after:package:createDeploymentArtifacts": this.instrument.bind(this),
      "before:deploy:function:packageFunction": this.instrument.bind(this)
    }
  }

  instrument() {
    const service = this.serverless.service;
    if (service.provider.name != "aws") {
      return;
    }
    const deploymentArtifact = this.serverless.service.package.artifact
    this.serverless.cli.log(`Instrumenting deploy artifact ${deploymentArtifact}...`)
    const result = spawnSync('java', ['-jar', this.instrumenter, deploymentArtifact, this.aspects, this.runtime], {
      cwd: process.cwd(),
      env: process.env,
      stdio: 'pipe',
      encoding: 'utf-8'
    })
    if (result.status == 0) {
      const instrumentedPackage = result.stdout.trim()
      this.serverless.service.package.artifact = instrumentedPackage // override
    } else {
      console.error(`Unable to instrument deployment artifact ${deploymentArtifact}`)
      console.error(result.stderr)
    }
  }
}

module.exports = JavaInstrumentPlugin;
