resource "aws_ecs_cluster" "webserver_ecs_cluster" {
  name = "donghang-webserver-ecs-cluster"

  setting {
    name  = "containerInsights"
    value = "enabled"
  }

  tags = {
    Name = "donghang-webserver-ecs-cluster"
  }
}
