output "external_alb_dns_name" {
  value = aws_alb.external_alb.dns_name
}

output "external_alb_zone_id" {
  value = aws_alb.external_alb.zone_id
}
