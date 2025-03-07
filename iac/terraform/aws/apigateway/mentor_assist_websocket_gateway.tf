####################################################
# MENTOR ASSIST WEBSOCKET GATEWAY                  #
####################################################
resource "aws_apigatewayv2_api" "mentor-assist-websocket-gateway" {
  name                       = "mentor-assist-websocket-gateway"
  description                = "API WebSockets for Mentor Assist (Staging)"
  protocol_type              = "WEBSOCKET"
  route_selection_expression = "$request.body.action"
  tags = {
    Terraform = "true"
    Team      = "Mentor Assist"
    Name      = "mentor-assist-websocket-gateway"
  }
}

resource "aws_apigatewayv2_integration" "mentor-assist-websocket-connect" {
  api_id                    = aws_apigatewayv2_api.mentor-assist-websocket-gateway.id
  integration_type          = "MOCK"
}

resource "aws_apigatewayv2_integration" "mentor-assist-websocket-disconnect" {
  api_id                    = aws_apigatewayv2_api.mentor-assist-websocket-gateway.id
  integration_type          = "MOCK"
}

resource "aws_apigatewayv2_integration" "mentor-assist-websocket-default" {
  api_id                    = aws_apigatewayv2_api.mentor-assist-websocket-gateway.id
  integration_type          = "MOCK"
}

resource "aws_apigatewayv2_route" "mentor-assist-websocket-connect" {
  api_id    = aws_apigatewayv2_api.mentor-assist-websocket-gateway.id
  route_key = "$connect"
  target    = "integrations/${aws_apigatewayv2_integration.mentor-assist-websocket-connect.id}"
  depends_on = [
    aws_apigatewayv2_integration.mentor-assist-websocket-connect
  ]
}

resource "aws_apigatewayv2_route" "mentor-assist-websocket-disconnect" {
  api_id    = aws_apigatewayv2_api.mentor-assist-websocket-gateway.id
  route_key = "$disconnect"
  target    = "integrations/${aws_apigatewayv2_integration.mentor-assist-websocket-disconnect.id}"
  depends_on = [
    aws_apigatewayv2_integration.mentor-assist-websocket-disconnect
  ]
}

resource "aws_apigatewayv2_route" "mentor-assist-websocket-default" {
  api_id    = aws_apigatewayv2_api.mentor-assist-websocket-gateway.id
  route_key = "$default"
  target    = "integrations/${aws_apigatewayv2_integration.mentor-assist-websocket-default.id}"
  depends_on = [
    aws_apigatewayv2_integration.mentor-assist-websocket-default
  ]
}

resource "aws_apigatewayv2_deployment" "mentor-assist-websocket-gateway" {
  api_id = aws_apigatewayv2_api.mentor-assist-websocket-gateway.id

  depends_on = [
    aws_apigatewayv2_route.mentor-assist-websocket-connect,
    aws_apigatewayv2_route.mentor-assist-websocket-disconnect,
    aws_apigatewayv2_route.mentor-assist-websocket-default
  ]
}

resource "aws_apigatewayv2_stage" "mentor-assist-websocket-gateway" {
  api_id        = aws_apigatewayv2_api.mentor-assist-websocket-gateway.id
  name          = "staging"
  deployment_id = aws_apigatewayv2_deployment.mentor-assist-websocket-gateway.id

  default_route_settings {
    data_trace_enabled       = true
    detailed_metrics_enabled = false
    logging_level            = "ERROR"
    throttling_rate_limit    = 10000
    throttling_burst_limit   = 5000
  }

  lifecycle {
    ignore_changes = [
      access_log_settings["destination_arn"],
      access_log_settings["format"]
    ]
  }
}
####################################################