output "appserver_ecs_task_role_arn" {
  value = aws_iam_role.appserver_ecs_task_role.arn
}

output "ecs_task_execution_role_arn" {
  value = aws_iam_role.ecs_task_execution_role.arn
}

output "ecs_instance_profile_name" {
  value = aws_iam_instance_profile.ecs_instance_profile.name
}

output "ssm_instance_profile_name" {
  value = aws_iam_instance_profile.ssm_instance_profile.name
}

output "webserver_ecs_task_role_arn" {
  value = aws_iam_role.webserver_ecs_task_role.arn
}
