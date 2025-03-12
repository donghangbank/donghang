terraform {
  backend "s3" {
    bucket         = "donghang-terraform-state-bucket"
    key            = "terraform.tfstate"
    region         = "ap-northeast-2"
    encrypt        = true
  }
}
