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

resource "aws_alb_target_group" "external_alb_target_group" {
  name                 = "donghang-external-alb-tg"
  port                 = 80
  protocol             = "HTTP"
  vpc_id               = var.vpc_id
  target_type          = "ip"
  deregistration_delay = 5

  health_check {
    path                = var.external_alb_health_check_path
    interval            = 30
    timeout             = 5
    healthy_threshold   = 3
    unhealthy_threshold = 2
  }

  lifecycle {
    create_before_destroy = true
  }

  tags = {
    Name = "donghang-external-alb-tg"
  }
}

resource "aws_alb_target_group" "internal_alb_target_group" {
  name                 = "donghang-internal-alb-tg"
  port                 = 8080
  protocol             = "HTTP"
  vpc_id               = var.vpc_id
  target_type          = "ip"
  deregistration_delay = 5

  health_check {
    path                = var.internal_alb_health_check_path
    interval            = 30
    timeout             = 5
    healthy_threshold   = 3
    unhealthy_threshold = 2
  }

  lifecycle {
    create_before_destroy = true
  }

  tags = {
    Name = "donghang-internal-alb-tg"
  }
}

resource "aws_alb_listener" "external_alb_http_listener" {
  load_balancer_arn = aws_alb.external_alb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type = "redirect"

    redirect {
      port        = "443"
      protocol    = "HTTPS"
      status_code = "HTTP_301"
    }
  }

  tags = {
    Name = "donghang-external-alb-http-listener"
  }
}

resource "aws_alb_listener" "external_alb_https_listener" {
  load_balancer_arn = aws_alb.external_alb.arn
  port              = 443
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2016-08"
  certificate_arn   = var.external_alb_certificate_arn

  default_action {
    type             = "forward"
    target_group_arn = aws_alb_target_group.external_alb_target_group.arn
  }

  tags = {
    Name = "donghang-external-alb-https-listener"
  }
}

resource "aws_alb_listener" "internal_alb_http_listener" {
  load_balancer_arn = aws_alb.internal_alb.arn
  port              = 8080
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_alb_target_group.internal_alb_target_group.arn
  }

  tags = {
    Name = "donghang-internal-alb-http-listener"
  }
}
