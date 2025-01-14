UPDATE study SET study.study_status = 'RECRUITMENT_CLOSED' and study.deleted_at = CURRENT_TIMESTAMP
WHERE study.id IN (
    SELECT study.id FROM study
                       INNER JOIN user_study us on study.id = us.study_id
                       INNER JOIN study_schedule ss on study.id = ss.study_id
    WHERE DATE(ss.start_date) = CURDATE() and ss.week_number = 1
    HAVING COUNT(DISTINCT us.id) < study.head_count
)
