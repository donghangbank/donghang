resource "aws_db_subnet_group" "dbsg" {
  name       = "donghang-dbsg"
  subnet_ids = var.database_subnets

  tags = {
    Name = "donghang-dbsg"
  }
}
