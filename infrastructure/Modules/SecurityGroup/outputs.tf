output "sg_mysql_id" {
  value = aws_security_group.sg_mysql.id
}

output "sg_valkey_id" {
  value = aws_security_group.sg_valkey.id
}

output "sg_vpce_ecr_id" {
  value = aws_security_group.sg_vpce_ecr.id
}
