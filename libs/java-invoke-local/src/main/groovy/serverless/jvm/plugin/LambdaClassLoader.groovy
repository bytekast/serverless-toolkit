package serverless.jvm.plugin

import java.nio.file.*

class LambdaClassLoader {

  static ClassLoader getClassLoader(String file) {
    getClassLoader(new File(file))
  }

  static ClassLoader getClassLoader(File file) {
    if (!file || !(file.name.endsWith('.jar') || file.name.endsWith('.zip'))) {
      throw new Exception("Unsupported file format")
    }

    if (!file.exists()) {
      throw new Exception("${file.name} does not exist")
    }

    if (file.name.endsWith('.jar')) {
      createUberJarClassLoader(file)
    } else {
      createZipFileClassLoader(file)
    }
  }

  private static createUberJarClassLoader(File uberJar) {
    URLClassLoader.newInstance([uberJar.toURI().toURL()].toArray() as URL[])
  }

  private static createZipFileClassLoader(File zipFile) {
    URLClassLoader.newInstance(extractZip(zipFile).toArray() as URL[])
  }

  private static extractZip(File zipFile) throws IOException {
    final tempDirectory = Files.createTempDirectory("")
    tempDirectory.toFile().deleteOnExit()
    final targetDir = Paths.get(tempDirectory.toUri())
    final zipUri = URI.create("jar:" + zipFile.toURI())
    final jars = []
    FileSystems.newFileSystem(zipUri, Collections.emptyMap()).withCloseable { zipFs ->
      final pathInZip = zipFs.getPath("/",)
      Files.walk(pathInZip).toList()?.each { Path filePath ->
        final relativePathInZip = pathInZip.relativize(filePath)
        final targetPath = targetDir.resolve(relativePathInZip.toString())
        Files.copy(filePath, targetPath, StandardCopyOption.REPLACE_EXISTING)
        targetPath.toFile().deleteOnExit()
        if (targetPath.toString().endsWith('.jar')) {
          jars.push(targetPath.toUri().toURL())
        }
      }
    }
    [targetDir.toUri().toURL(), jars].flatten()
  }
}
