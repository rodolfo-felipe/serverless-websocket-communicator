##############################################
# mentor_assist_websocket_session                     #
##############################################
resource "aws_dynamodb_table" "mentor_assist_websocket_session" {
  name         = "mentor_assist_websocket_session"
  hash_key     = "caller_id"
  range_key    = "context"
  billing_mode = "PAY_PER_REQUEST"

  attribute {
    name = "caller_id"
    type = "S"
  }

  attribute {
    name = "context"
    type = "S"
  }

  attribute {
    name = "connection_id"
    type = "S"
  }

  global_secondary_index {
    name            = "connection_id_index"
    hash_key        = "connection_id"
    projection_type = "KEYS_ONLY"
  }

  tags = {
    Name = "mentor_assist_websocket_session"
    Team = "Mentor Assist"
  }

  ttl {
    attribute_name = "expiration_date"
    enabled        = true
  }
}