WITH eligible_studies AS (
    SELECT s.id
    FROM study AS s
             INNER JOIN user_study us ON s.id = us.study_id
             INNER JOIN study_schedule ss ON s.id = ss.study_id
    WHERE DATE(ss.start_date) = CURDATE() AND ss.week_number = 1
    GROUP BY s.id, s.head_count
    HAVING COUNT(DISTINCT us.id) < s.head_count
)
UPDATE study
SET study_status = 'RECRUITMENT_CLOSED', deleted_at = CURRENT_TIMESTAMP
WHERE id IN (SELECT id FROM eligible_studies);

