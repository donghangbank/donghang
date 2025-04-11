# 🔧 동행 프로젝트 포팅 가이드

이 문서는 동행 프로젝트를 포팅할 때 필요한 기본 설치 및 환경 설정 절차를 단계별로 안내합니다.

---

## 📦 공통 설치

### 1. Chocolatey 설치 (Windows용 패키지 매니저)

**PowerShell (관리자 권한)에서 실행:**

```powershell
Set-ExecutionPolicy Bypass -Scope Process -Force; `
[System.Net.ServicePointManager]::SecurityProtocol = `
[System.Net.ServicePointManager]::SecurityProtocol -bor 3072; `
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

**설치 확인:**

```powershell
choco
```

**필수 패키지 설치:**

```powershell
choco install git
choco install zip
```

### 2. Docker Desktop 설치

설치 링크: Docker Desktop 설치 가이드 (Windows)  
참고: Docker Desktop 로그인은 선택 사항입니다.

### 3. Git 사용자 정보 설정

```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

### 4. 프로젝트 클론

```bash
git clone https://lab.ssafy.com/s12-fintech-finance-sub1/S12P21A701.git
```

## 🎨 Frontend 환경 구성

### 1. Node.js 설치 (via NVM)

**NVM 설치:**

```powershell
choco install nvm
```

**설치 확인 (Git Bash):**

```bash
nvm -v
```

**Node.js LTS 버전 설치 및 설정:**

```bash
nvm install lts
nvm use 22.14.0
```

**npm 최신 버전 설치:**

```bash
npm i -g npm@11.0.0
```

### 2. 프로젝트 설정

**프로젝트 디렉토리 이동:**

```bash
cd S12P21A701/frontend
```

**환경변수 파일 생성 (.env):**

```env
VITE_API_BASE_URL=https://api.donghang.click
VITE_API_BASE_URL_LOCAL=http://localhost:8080
VITE_CARD_NUMBER=
```

**패키지 설치:**

```bash
npm install
```

**빌드 (Windows):**

```bash
npm run build:win
```

**프로젝트 실행 (Electron):**

```bash
npm run dev
```

## 🛠 Backend 환경 구성

### 1. Java 설치 (via SDKMAN)

**SDKMAN 설치 (Git Bash):**

```bash
curl -s "https://get.sdkman.io" | bash
```

**설치 후 새 터미널에서 실행:**

```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

**Java 버전 확인 및 설치:**

```bash
sdk list java
sdk install java 17.0.14-librca
sdk use java 17.0.14-librca
sdk default java 17.0.14-librca
```

**환경변수 설정 (Windows):**  
`JAVA_HOME=C:\Users\{사용자명}\.sdkman\candidates\java\current`

### 2. IntelliJ 설정

- `S12P21A701` 폴더 열기  
- `Settings > Build, Execution, Deployment > Build Tools > Gradle`에서 `Gradle JVM`을 Java 17로 설정

### 3. 환경변수 설정

**backend 폴더 내 .env 파일 생성**

`dev.env` 예시:

```env
SPRING_APPLICATION_NAME=donghang-api

MYSQL_DATASOURCE_DRIVER=com.mysql.cj.jdbc.Driver
MYSQL_DATASOURCE_URL=
MYSQL_DATASOURCE_USERNAME=
MYSQL_DATASOURCE_PASSWORD=

MYSQL_JPA_SHOW_SQL=true
MYSQL_JPA_DDL_AUTO=update

S3_REGION=
S3_BUCKET=
S3_ACCESS_KEY=
S3_SECRET_KEY=

spring.data.redis.port=
spring.data.redis.host=
REDIS_HOST=
REDIS_PORT=

DONGHANG_CASH_ACCOUNT_ID=
```

`test.env` 예시:

```env
H2_DATASOURCE_DRIVER=org.h2.Driver
H2_DATASOURCE_URL=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
H2_DATASOURCE_USERNAME=
H2_DATASOURCE_PASSWORD=
H2_MAXIMUM_POOL_SIZE=50
H2_MINIMUM_IDLE=10

MYSQL_JPA_SHOW_SQL=true
MYSQL_JPA_DDL_AUTO=update

S3_REGION=
S3_BUCKET=
S3_ACCESS_KEY=
S3_SECRET_KEY=

spring.data.redis.port=
spring.data.redis.host=
REDIS_HOST=
REDIS_PORT=

DONGHANG_CASH_ACCOUNT_ID=
```

**.gitignore 추가:**

```gitignore
*.env
```

### 4. 실행 환경 설정

- `Edit Configurations > Active profiles`에 `dev` 입력  
- `Modify Options > Environment variables`에 `backend/dev.env` 경로 입력

## 🤖 AI 서비스 구성

### 1. Python 설치

- 버전: Python 3.12.8  
- 다운로드: https://www.python.org/downloads/release/python-3128/

**설치 확인:**

```bash
python --version
```

### 2. 가상환경 설정

```bash
cd S12P21A701/ai
python -m venv venv
venv/Scripts/activate  # Windows
# 비활성화는 deactivate
```

### 3. 환경변수 파일 생성 (.env)

```env
OPEN_AI_API_KEY=your-api-key-here
```

### 4. 의존성 설치

```bash
pip install -r requirements.txt
```

### 5. 실행

```bash
uvicorn main:app --reload           # 기본 포트 8000
uvicorn main:app --reload --port=8001  # 포트 지정 시
```

## 🌐 Infra 세팅

`infrastructure` 폴더 내에 `terraform.tfvars` 파일 생성:

```hcl
availability_zones              = ["ap-northeast-2a", "ap-northeast-2c"]
aws_profile                     = "donghang-terraform"
aws_region                      = "ap-northeast-2"
database_subnet_cidr_block      = ["10.0.160.0/20", "10.0.176.0/20"]
domain_name                     = "donghang.click"
external_alb_health_check_path  = "/actuator/health"
internal_alb_health_check_path  = "/actuator/health"
mysql_password                  = ""
mysql_username                  = ""
private_subnet_cidr_block       = ["10.0.128.0/20", "10.0.144.0/20"]
public_subnet_cidr_block        = ["10.0.0.0/20", "10.0.16.0/20"]
route53_zone_id                 = "Z0706549337MQTNOA3OQ1"
vpc_cidr                        = "10.0.0.0/16"
```

**AWS CLI 설정:**  
IAM 사용자 키를 발급받아 CLI에 등록하세요.  
([참고: AWS IAM 사용자 가이드](https://docs.aws.amazon.com/ko_kr/IAM/latest/UserGuide/id_credentials_access-keys.html))