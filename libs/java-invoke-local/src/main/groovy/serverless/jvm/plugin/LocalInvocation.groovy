package serverless.jvm.plugin

import com.amazonaws.services.lambda.runtime.*
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonSlurper

class LocalInvocation {

  static final ObjectMapper mapper = new ObjectMapper()

  static invoke(LambdaFunction lambda, String input, String functionName = null) {

    if (RequestStreamHandler.isAssignableFrom(lambda.clazz)) {
      def inputStream = new ByteArrayInputStream(input.getBytes())
      def outputStream = new ByteArrayOutputStream()
      lambda.instance."${lambda.method}"(inputStream, outputStream, mockContext(lambda))
      return outputStream
    } else {
      if (lambda.parameterType.isPrimitive() || isWrapperType(lambda.parameterType) || lambda.parameterType == String) {
        return lambda.instance."${lambda.method}"(input?.asType(lambda.parameterType), mockContext(functionName ?: lambda.method))
      } else {

        if (input == null) {
          if (lambda.hasContext) {
            return lambda.instance."${lambda.method}"(input, mockContext(lambda.method))
          } else {
            return lambda.instance."${lambda.method}"(input)
          }
        }

        try {
          new JsonSlurper().parseText(input)
        } catch (e) {
          throw new Exception("Unable to convert input \"${input}\" to ${lambda.parameterType.name}")
        }
        final objectInput = mapper.readValue(input, lambda.parameterType)
        if (lambda.hasContext) {
          return lambda.instance."${lambda.method}"(objectInput, mockContext(lambda.method))
        } else {
          return lambda.instance."${lambda.method}"(objectInput)
        }
      }
    }
  }

  private static boolean isWrapperType(Class clazz) {
    return clazz == Boolean.class ||
      clazz == Integer.class ||
      clazz == Character.class ||
      clazz == Byte.class ||
      clazz == Short.class ||
      clazz == Double.class ||
      clazz == Long.class ||
      clazz == Float.class
  }

  private static mockContext(final functionName) {
    new Context() {
      @Override
      String getAwsRequestId() {
        return UUID.randomUUID().toString()
      }

      @Override
      String getLogGroupName() {
        return "local-log-group-name"
      }

      @Override
      String getLogStreamName() {
        return "local-log-stream-name"
      }

      @Override
      String getFunctionName() {
        return functionName
      }

      @Override
      String getFunctionVersion() {
        return "0.0.0-alpha"
      }

      @Override
      String getInvokedFunctionArn() {
        return "local-function-arn"
      }

      @Override
      CognitoIdentity getIdentity() {
        return new CognitoIdentity() {
          @Override
          String getIdentityId() {
            return "local-cognito-id"
          }

          @Override
          String getIdentityPoolId() {
            return "local-cognito-pool-id"
          }
        }
      }

      @Override
      ClientContext getClientContext() {
        return new ClientContext() {
          @Override
          Client getClient() {
            return new Client() {
              @Override
              String getInstallationId() {
                return "local-installation-id"
              }

              @Override
              String getAppTitle() {
                return "local-app-title"
              }

              @Override
              String getAppVersionName() {
                return "local-app-version-name"
              }

              @Override
              String getAppVersionCode() {
                return "local-app-version-code"
              }

              @Override
              String getAppPackageName() {
                return "local-app-package-name"
              }
            }
          }

          @Override
          Map<String, String> getCustom() {
            return Collections.emptyMap()
          }

          @Override
          Map<String, String> getEnvironment() {
            return Collections.emptyMap()
          }
        }
      }

      @Override
      int getRemainingTimeInMillis() {
        return 0
      }

      @Override
      int getMemoryLimitInMB() {
        return 0
      }

      @Override
      LambdaLogger getLogger() {
        return new LambdaLogger() {
          @Override
          void log(String message) {
            println message
          }

          @Override
          void log(byte[] message) {
            println new String(message)
          }
        }
      }
    }
  }
}
