#!/usr/bin/env bash
set -euo pipefail

# Docker secret에서 비번 읽어 Spring 환경변수로 전달
if [[ -f /run/secrets/app_db_password ]]; then
  export SPRING_DATASOURCE_PASSWORD="$(cat /run/secrets/app_db_password)"
fi

exec java -jar /app/app.jar
