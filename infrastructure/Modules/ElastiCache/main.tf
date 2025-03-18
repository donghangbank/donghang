resource "aws_elasticache_subnet_group" "elasticachesg" {
  name       = "donghang-elasticachesg"
  subnet_ids = var.database_subnets

  tags = {
    Name = "donghang-elasticachesg"
  }
}
