'use strict';

const {spawnSync} = require("child_process")
const jar = `${__dirname}/../build/libs/java-invoke-local-all.jar`

const invokeJavaLocal = (args) => {
  const version = getJavaVersion()
  const vmArgs = []
  if (!version.startsWith('1.8')) {
    vmArgs.push('--illegal-access=deny', '--add-opens', 'java.base/java.lang=ALL-UNNAMED', '--add-opens', 'java.base/java.lang.invoke=ALL-UNNAMED')
  }
  spawnSync('java', ['-jar', ...vmArgs, jar, ...args], {
    cwd: process.cwd(),
    env: process.env,
    stdio: 'inherit',
    encoding: 'utf-8'
  })
}

const getJavaVersion = () => {
  const childProcess = spawnSync('java', ['-version'], {
    cwd: process.cwd(),
    env: process.env,
    stdio: 'pipe',
    encoding: 'utf-8'
  })

  const result = childProcess.stdout || childProcess.stderr
  const data = result.toString().split('\n')[0]
  const version = new RegExp('version').test(data) ? data.split(' ')[2].replace(/"/g, '') : false
  if (version != false) {
    return version
  } else {
    return 'unknown'
  }
}

module.exports = {
  getJavaVersion,
  invokeJavaLocal
}