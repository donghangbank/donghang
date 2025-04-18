output "database_subnets" {
  value = aws_subnet.database_subnets[*].id
}

output "private_subnets" {
  value = aws_subnet.private_subnets[*].id
}

output "public_subnets" {
  value = aws_subnet.public_subnets[*].id
}

output "vpc_id" {
  value = aws_vpc.vpc.id
}

output "vpce_s3_id" {
  value = aws_vpc_endpoint.vpce_s3.id
}
