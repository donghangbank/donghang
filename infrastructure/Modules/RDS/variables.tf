variable "database_subnets" {
  type = list(string)
}

variable "kms_rds_key_arn" {
  type = string
}

variable "mysql_password" {
  type = string
}

variable "mysql_username" {
  type = string
}

variable "sg_mysql_id" {
  type = string
}
