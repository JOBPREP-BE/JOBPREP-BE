CREATE INDEX study_id_study_status__index
ON study (study_status desc, deleted_at asc, id desc);