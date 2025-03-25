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

resource "aws_ecs_cluster" "appserver_ecs_cluster" {
  name = "donghang-appserver-ecs-cluster"

  setting {
    name  = "containerInsights"
    value = "enabled"
  }

  tags = {
    Name = "donghang-appserver-ecs-cluster"
  }
}
