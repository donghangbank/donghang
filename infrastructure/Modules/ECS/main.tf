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

resource "aws_ecs_task_definition" "webserver_ecs_task_definition" {
  family             = "donghang-webserver-ecs-task"
  network_mode       = "awsvpc"
  execution_role_arn = var.ecs_task_execution_role_arn
  task_role_arn      = var.webserver_ecs_task_role_arn

  container_definitions = jsonencode([
    {
      name      = "webserver-container",
      image     = "nginx:latest",
      memory    = 768,
      cpu       = 512,
      essential = true,
      portMappings = [
        {
          containerPort = 80,
          protocol      = "tcp"
        }
      ],
      logConfiguration = {
        logDriver = "awslogs",
        options = {
          "awslogs-group"         = "${var.webserver_log_group_name}",
          "awslogs-region"        = "${var.aws_region}",
          "awslogs-stream-prefix" = "web"
        }
      },
      command = [
        "/bin/sh",
        "-c",
        join("\n", [
          "cat <<'EOF' > /etc/nginx/nginx.conf",
          "events {}",
          "",
          "http {",
          "    server {",
          "        listen 80;",
          "",
          "        location / {",
          "            proxy_pass http://${var.internal_alb_dns_name}:8080;",
          "            proxy_set_header Host $host;",
          "            proxy_set_header X-Real-IP $remote_addr;",
          "            proxy_intercept_errors off;",
          "        }",
          "    }",
          "}",
          "EOF",
          "nginx -g 'daemon off;'"
        ])
      ]
    }
  ])

  tags = {
    Name = "donghang-webserver-ecs-task"
  }
}

resource "aws_ecs_task_definition" "appserver_ecs_task_definition" {
  family             = "donghang-appserver-ecs-task"
  network_mode       = "awsvpc"
  execution_role_arn = var.ecs_task_execution_role_arn
  task_role_arn      = var.appserver_ecs_task_role_arn

  container_definitions = jsonencode([
    {
      name      = "appserver-container",
      image     = "${var.ecr_repository_url}:latest",
      memory    = 768,
      cpu       = 512,
      essential = true,
      portMappings = [{
        containerPort = 8080,
        protocol      = "tcp"
      }],
      logConfiguration = {
        logDriver = "awslogs",
        options = {
          "awslogs-group"         = "${var.appserver_log_group_name}",
          "awslogs-region"        = "${var.aws_region}",
          "awslogs-stream-prefix" = "app"
        }
      },
      environmentFiles = [
        {
          value = "${var.asset_bucket_arn}/environments/.env",
          type  = "s3"
        }
      ]
    }
  ])

  tags = {
    Name = "donghang-appserver-ecs-task"
  }
}
