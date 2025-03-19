variable "private_subnets" {
  type = list(string)
}

variable "public_subnets" {
  type = list(string)
}

variable "sg_external_alb_id" {
  type = string
}

variable "sg_internal_alb_id" {
  type = string
}
