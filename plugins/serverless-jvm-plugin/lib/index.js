'use strict';

const {invokeJavaLocal} = require('java-invoke-local')

class JmvPlugin {

  constructor(serverless, options) {
    this.serverless = serverless
    this.options = options
    this.serverlessPath = `${this.serverless.config.servicePath}/.serverless`
    serverless.variables.populateService(serverless.pluginManager.cliOptions).then(() => {
      this.functions = (serverless.service || {}).functions
      this.serviceArtifact = (serverless.service.package || {}).artifact
    })

    this.commands = {
      jvm: {
        commands: {
          'invoke-local': {
            lifecycleEvents: ['invoke'],
            options: {
              'function-name': {
                usage: 'Name of the function',
                shortcut: 'f',
                required: true,
              },
              'data': {
                usage: 'Input data',
                shortcut: 'd',
              },
              'path': {
                usage: 'Path to JSON holding input data',
                shortcut: 'p',
              },
              'json-output': {
                usage: ' convert output to json',
              },
            },
          }
        }
      }
    }

    this.hooks = {
      "jvm:invoke-local:invoke": this.invoke.bind(this)
    }
  }

  invoke() {
    const func = this.functions[this.options['function-name']]
    const fallbackJar = `${this.serverlessPath}/${this.options['function-name']}.jar`
    const artifact = (func.package || {}).artifact || this.serviceArtifact || fallbackJar
    const args = [
      '-c', func.handler, '-a', artifact, '-f', this.options['function-name']
    ]
    if (this.options.path) {
      args.push('-i', this.options.path)
    } else if (this.options.data) {
      args.push('-d', this.options.data)
    }
    if (this.options['json-output']) {
      args.push('--json-output')
    }

    const result = invokeJavaLocal(args)
    console.log(result)
  }
}

module.exports = JmvPlugin
