output "external_alb_arn" {
  value = aws_alb.external_alb.arn
}

output "external_alb_dns_name" {
  value = aws_alb.external_alb.dns_name
}

output "external_alb_zone_id" {
  value = aws_alb.external_alb.zone_id
}
