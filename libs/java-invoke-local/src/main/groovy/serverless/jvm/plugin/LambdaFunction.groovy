package serverless.jvm.plugin

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.RequestStreamHandler

class LambdaFunction {
  Class clazz
  Object instance
  String method
  Class returnType
  Class parameterType
  Boolean hasContext
  Boolean hasOutput

  static LambdaFunction create(String handler, ClassLoader classLoader) {
    def (String className, String methodName) = handler.tokenize('::')

    final function = new LambdaFunction()
    final clazz = Class.forName(className, true, classLoader)
    function.clazz = clazz
    if (methodName) {
      final methods = clazz.metaClass.getMethods().findAll { it.name == methodName }
      if (methods.size() < 1) {
        throw new Exception('Method name does not exist')
      }

      if (methods.size() > 1) {
        throw new Exception("Multiple method named '${methodName}' detected")
      }

      def (method) = methods
      function.method = method.name
      function.returnType = method.returnType
      final parameterTypes = method.nativeParameterTypes
      if (parameterTypes.size() < 1 || parameterTypes.size() > 3) {
        throw new Exception("Handler method must have 1 to 3 parameters")
      }

      def first = parameterTypes?.size() > 0 ? parameterTypes[0] : null
      def second = parameterTypes?.size() > 1 ? parameterTypes[1] : null
      def third = parameterTypes?.size() > 2 ? parameterTypes[2] : null
      function.parameterType = first
      if ((second != null && third == null && second != Context) || (third != null && third != Context)) {
        throw new Exception("The second or third handler method parameter must be of type com.amazonaws.services.lambda.runtime.Context")
      }

      if (second || third) {
        function.hasContext = true
      }
      if (third) {
        if (second != OutputStream || third != Context) {
          throw new Exception("If third third handler method parameter is com.amazonaws.services.lambda.runtime.Context, second must be java.io.OutputStream")
        }
        function.hasOutput = true
      }
    } else {
      if (!(RequestHandler.isAssignableFrom(clazz) || RequestStreamHandler.isAssignableFrom(clazz))) {
        throw new Exception("Unable to detect handler method name")
      }

      final method = clazz.metaClass.getMethods().find { it.name == 'handleRequest' }
      function.method = method?.name
      function.returnType = method?.returnType
      function.parameterType = method?.nativeParameterTypes?.first()
      function.hasContext = true
      function.hasOutput = false
    }

    function.instance = clazz.newInstance([].toArray())
    function
  }
}
