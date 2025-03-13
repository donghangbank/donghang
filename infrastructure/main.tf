module "networing" {
  source   = "./Modules/Networking"
  vpc_cidr = var.vpc_cidr
}

module "s3" {
  source = "./Modules/S3"
}
