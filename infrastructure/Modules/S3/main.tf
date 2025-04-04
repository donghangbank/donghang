resource "aws_s3_bucket" "asset_bucket" {
  bucket = "donghang-bucket-${terraform.workspace}"

  tags = {
    Name = "donghang-bucket-${terraform.workspace}"
  }
}

resource "aws_s3_bucket" "release_bucket" {
  bucket = "donghang-bucket-release"

  tags = {
    Name = "donghang-bucket-release"
  }
}

resource "aws_s3_bucket_public_access_block" "asset_public_access" {
  bucket = aws_s3_bucket.asset_bucket.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

resource "aws_s3_bucket_public_access_block" "release_public_access" {
  bucket = aws_s3_bucket.release_bucket.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

resource "aws_s3_bucket_policy" "asset_bucket_policy" {
  bucket = aws_s3_bucket.asset_bucket.id

  depends_on = [
    aws_s3_bucket_public_access_block.asset_public_access
  ]

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid       = "AllowPrivateWriteAccess"
        Effect    = "Allow"
        Principal = "*"
        Action = [
          "s3:PutObject"
        ]
        Resource = "${aws_s3_bucket.asset_bucket.arn}/*"
        Condition = {
          StringEquals = {
            "aws:SourceVpce" = "${var.vpce_s3_id}"
          }
        }
      },
      {
        Sid       = "AllowPublicReadAccess"
        Effect    = "Allow"
        Principal = "*"
        Action = [
          "s3:GetObject"
        ]
        Resource = "${aws_s3_bucket.asset_bucket.arn}/*"
      }
    ]
  })
}

resource "aws_s3_bucket_policy" "release_bucket_policy" {
  bucket = aws_s3_bucket.release_bucket.id

  depends_on = [
    aws_s3_bucket_public_access_block.release_public_access
  ]

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid       = "AllowObjectLevelAccess"
        Effect    = "Allow"
        Principal = "*"
        Action = [
          "s3:AbortMultipartUpload",
          "s3:GetObject",
          "s3:GetObjectAcl",
          "s3:GetObjectVersion",
          "s3:ListMultipartUploadParts",
          "s3:PutObject",
          "s3:PutObjectAcl"
        ]
        Resource = "${aws_s3_bucket.release_bucket.arn}/*"
      },
      {
        Sid       = "AllowBucketListingAccess"
        Effect    = "Allow"
        Principal = "*"
        Action = [
          "s3:ListBucket",
          "s3:ListBucketMultipartUploads"
        ]
        Resource = "${aws_s3_bucket.release_bucket.arn}"
      }
    ]
  })
}
