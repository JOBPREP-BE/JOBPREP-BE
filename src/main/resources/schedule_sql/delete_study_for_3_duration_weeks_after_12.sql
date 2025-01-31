WITH temp_study_ids AS (
    SELECT s.id
    FROM study AS s
             INNER JOIN study_schedule ss ON s.id = ss.study_id
    WHERE DATE_ADD(ss.start_date, INTERVAL 1 DAY) = CURDATE()
      AND ss.week_number = s.duration_weeks
)
UPDATE study
SET study_status = 'FINISHED', deleted_at = CURRENT_TIMESTAMP
WHERE id IN (SELECT id FROM temp_study_ids);
