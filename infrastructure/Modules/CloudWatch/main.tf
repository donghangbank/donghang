resource "aws_cloudwatch_log_group" "webserver_log_group" {
  name              = "/ecs/donghang-webserver-service"
  retention_in_days = 7

  tags = {
    Name = "donghang-webserver-log-group"
  }
}

resource "aws_cloudwatch_log_group" "appserver_log_group" {
  name              = "/ecs/donghang-appserver-service"
  retention_in_days = 7

  tags = {
    Name = "donghang-appserver-log-group"
  }
}
