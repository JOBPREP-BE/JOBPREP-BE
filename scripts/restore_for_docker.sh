#!/bin/bash

# 복구 스크립트 for Local Docker
# 사용법: ./restore_local.sh <볼륨 이름>

VOLUME_NAME=$1
BACKUP_DIR=./backup
DIRECTORY_NAME=BACKUP_$(date +%Y%m%d)
MOUNT_DIR=${BACKUP_DIR}/${DIRECTORY_NAME}/
FILE_NAME=backup_${VOLUME_NAME}_$(date +%Y%m%d)*.tar
BACKUP_FILE=${MOUNT_DIR}/${FILE_NAME}

# 입력값 검증
if [ -z "$VOLUME_NAME" ]; then
  echo "사용법: $0 <볼륨 이름>"
  exit 1
fi

# 백업 파일 존재 확인
if [ -f "$BACKUP_FILE" ]; then
  echo "백업 파일을 찾을 수 없습니다: $BACKUP_FILE"
  exit 1
fi

# Docker에서 복구 수행
docker run --rm \
  -v ${VOLUME_NAME}:/volume \
  -v ${PWD}/${MOUNT_DIR}:/backup \
  alpine \
  sh -c "tar xvf /backup/$FILE_NAME -C /volume"

if [ $? -eq 0 ]; then
  echo "복구 완료: ${BACKUP_FILE} -> ${VOLUME_NAME}"
else
  echo "복구 실패"
  exit 1
fi
