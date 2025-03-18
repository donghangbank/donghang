module "ecr" {
  source = "./Modules/ECR"
}

module "elasticache" {
  source           = "./Modules/ElastiCache"
  database_subnets = module.networing.database_subnets
}

module "networing" {
  source                     = "./Modules/Networking"
  availability_zones         = var.availability_zones
  aws_region                 = var.aws_region
  database_subnet_cidr_block = var.database_subnet_cidr_block
  private_subnet_cidr_block  = var.private_subnet_cidr_block
  public_subnet_cidr_block   = var.public_subnet_cidr_block
  sg_vpce_ecr_id             = module.security_group.sg_vpce_ecr_id
  vpc_cidr                   = var.vpc_cidr
}

module "rds" {
  source           = "./Modules/RDS"
  database_subnets = module.networing.database_subnets
  mysql_password   = var.mysql_password
  mysql_username   = var.mysql_username
  sg_mysql_id      = module.security_group.sg_mysql_id
}

module "s3" {
  source     = "./Modules/S3"
  vpce_s3_id = module.networing.vpce_s3_id
}

module "security_group" {
  source                     = "./Modules/SecurityGroup"
  database_subnet_cidr_block = var.database_subnet_cidr_block
  private_subnet_cidr_block  = var.private_subnet_cidr_block
  public_subnet_cidr_block   = var.public_subnet_cidr_block
  vpc_id                     = module.networing.vpc_id
}
