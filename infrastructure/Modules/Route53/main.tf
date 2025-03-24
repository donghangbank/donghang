resource "aws_route53_record" "external_alb_dns" {
  zone_id = var.route53_zone_id
  name    = "api.${var.domain_name}"
  type    = "A"

  alias {
    name                   = var.external_alb_dns_name
    zone_id                = var.external_alb_zone_id
    evaluate_target_health = true
  }
}
