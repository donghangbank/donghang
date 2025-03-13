module "networing" {
  source                     = "./Modules/Networking"
  availability_zones         = var.availability_zones
  aws_region                 = var.aws_region
  database_subnet_cidr_block = var.database_subnet_cidr_block
  private_subnet_cidr_block  = var.private_subnet_cidr_block
  public_subnet_cidr_block   = var.public_subnet_cidr_block
  vpc_cidr                   = var.vpc_cidr
}

module "s3" {
  source     = "./Modules/S3"
  vpce_s3_id = module.networing.vpce_s3_id
}
