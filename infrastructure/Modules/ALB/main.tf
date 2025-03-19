resource "aws_alb" "external_alb" {
  name               = "donghang-external-alb"
  subnets            = var.public_subnets
  security_groups    = [var.sg_external_alb_id]
  load_balancer_type = "application"
  internal           = false
  enable_http2       = true
  idle_timeout       = 30

  tags = {
    Name = "donghang-external-alb"
  }
}

resource "aws_alb" "internal_alb" {
  name               = "donghang-internal-alb"
  subnets            = var.private_subnets
  security_groups    = [var.sg_internal_alb_id]
  load_balancer_type = "application"
  internal           = true
  enable_http2       = true
  idle_timeout       = 30

  tags = {
    Name = "donghang-internal-alb"
  }
}
