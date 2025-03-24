provider "aws" {
  region = "ap-northeast-2"
  alias  = "seoul"
}

resource "aws_acm_certificate" "external_alb_certificate" {
  provider          = aws.seoul
  domain_name       = "api.${var.domain_name}"
  validation_method = "DNS"

  tags = {
    Name = "donghang-external-alb-certificate"
  }
}

resource "aws_route53_record" "external_alb_route53_record" {
  for_each = { for dvo in aws_acm_certificate.external_alb_certificate.domain_validation_options : dvo.domain_name => dvo }
  name     = each.value.resource_record_name
  type     = each.value.resource_record_type
  zone_id  = var.route53_zone_id
  records  = [each.value.resource_record_value]
  ttl      = 300
}
