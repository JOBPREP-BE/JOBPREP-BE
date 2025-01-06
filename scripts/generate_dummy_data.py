import pandas as pd
import random
from faker import Faker

# Faker 초기화
fake = Faker()

# 데이터 개수 설정
num_records = 10_000_000  # 천만 개

# ENUM 타입 필드 값 설정
positions = ['상관없음', '개발', '디자인', '기획', '마케팅', '영업', '재무/회계', '인사']
study_statuses = ['진행중', '모집중', '모집완료']

# 랜덤 데이터 생성 함수
def generate_data(num_records):
    data = []
    for i in range(1, num_records + 1):
        study_name = f"Study_{i}"  # 고유한 study_name
        position = random.choice(positions)
        study_status = random.choice(study_statuses)
        head_count = random.randint(1, 10)  # 1~10 사이 랜덤 값
        duration_weeks = random.randint(1, 52)  # 1~52 사이 랜덤 값
        google_link = fake.url() if random.random() > 0.5 else None
        discord_link = fake.url() if random.random() > 0.5 else None
        kakao_link = fake.url() if random.random() > 0.5 else None
        created_at = fake.date_time_between(start_date="-5y", end_date="now")  # 최근 5년
        deleted_at = (
            fake.date_time_between(start_date=created_at, end_date="now")
            if random.random() > 0.9
            else None
        )  # 일부 데이터만 삭제됨

        data.append(
            [
                i,  # id
                study_name,
                position,
                study_status,
                head_count,
                duration_weeks,
                google_link,
                discord_link,
                kakao_link,
                created_at,
                deleted_at,
            ]
        )
    return data

# 데이터 생성 및 저장
if __name__ == "__main__":
    print("데이터 생성 중...")
    column_names = [
        "id",
        "study_name",
        "position",
        "study_status",
        "head_count",
        "duration_weeks",
        "google_link",
        "discord_link",
        "kakao_link",
        "created_at",
        "deleted_at",
    ]

    # 데이터를 생성하고 저장
    batch_size = 1_000_000  # 메모리 문제를 방지하기 위한 배치 처리
    for batch in range(num_records // batch_size):
        print(f"{batch * batch_size} ~ {(batch + 1) * batch_size - 1} 데이터 생성 중...")
        batch_data = generate_data(batch_size)
        df = pd.DataFrame(batch_data, columns=column_names)

        # CSV 저장 (추가 모드)
        if batch == 0:
            df.to_csv("study_data.csv", index=False, mode="w", encoding="utf-8-sig")
        else:
            df.to_csv("study_data.csv", index=False, mode="a", header=False, encoding="utf-8-sig")

    print("CSV 파일 저장 완료: study_data.csv")