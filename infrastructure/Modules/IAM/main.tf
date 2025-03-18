resource "aws_iam_role" "ssm_role" {
  name = "donghang-ssm-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect    = "Allow",
        Principal = { Service = "ec2.amazonaws.com" },
        Action    = "sts:AssumeRole"
      }
    ]
  })

  tags = {
    Name = "donghang-ssm-role"
  }
}

resource "aws_iam_role_policy_attachment" "ssm_attach" {
  role       = aws_iam_role.ssm_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

resource "aws_iam_instance_profile" "ssm_instance_profile" {
  name = "donghang-ssm-instance-profile"
  role = aws_iam_role.ssm_role.name

  tags = {
    Name = "donghang-ssm-instance-profile"
  }
}
