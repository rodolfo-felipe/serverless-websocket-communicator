####################################################
# MENTOR ASSIST SNS TOPIC                          #
####################################################
resource "aws_sns_topic" "mentor-assist-websocket-message-publisher" {
  name = "mentor-assist-websocket-message-publisher"
}
####################################################