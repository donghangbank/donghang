resource "aws_kms_key" "kms_rds_key" {
  enable_key_rotation     = true
  deletion_window_in_days = 20
}
