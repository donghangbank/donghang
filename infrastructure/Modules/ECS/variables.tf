variable "aws_region" {
  type = string
}

variable "ecs_task_execution_role_arn" {
  type = string
}

variable "internal_alb_dns_name" {
  type = string
}

variable "webserver_ecs_task_role_arn" {
  type = string
}

variable "webserver_log_group_name" {
  type = string
}
