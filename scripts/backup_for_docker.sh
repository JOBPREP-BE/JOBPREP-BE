#!/bin/bash

# 환경변수 설정
VOLUME_NAME=jobprep-be_db_data
BACKUP_DIR=./backup
DIRECTORY_NAME=BACKUP_$(date +%Y%m%d)
FILE_NAME=backup_${VOLUME_NAME}_$(date +%Y%m%d%H%M%S).tar
MOUNT_DIR=${BACKUP_DIR}/${DIRECTORY_NAME}/
BACKUP_FILE=${BACKUP_DIR}/${DIRECTORY_NAME}/${FILE_NAME}

# 백업 디렉토리 생성 (필요시)
mkdir -p $BACKUP_DIR
mkdir -p $BACKUP_DIR/$DIRECTORY_NAME

# 백업 작업
echo "Backing up volume ${VOLUME_NAME} to ${BACKUP_FILE}..."

docker run --rm \
  -v ${VOLUME_NAME}:/volume \
  -v ${PWD}/${MOUNT_DIR}:/backup \
  alpine \
  tar cvf /backup/${FILE_NAME} -C /volume .

echo "Backup completed: ${BACKUP_FILE}"
