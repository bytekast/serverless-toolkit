package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;
import com.google.gson.Gson;
import com.serverless.model.Body;

public class Hello implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent> {

  private static final Gson gson = new Gson();

  @Override
  public APIGatewayV2ProxyResponseEvent handleRequest(APIGatewayV2ProxyRequestEvent event, Context context) {
    APIGatewayV2ProxyResponseEvent response = new APIGatewayV2ProxyResponseEvent();
    response.setStatusCode(200);
    response.setBody(gson.toJson(new Body("Go Serverless v1.x! Your function executed successfully!")));
    return response;
  }
}
