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
