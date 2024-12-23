UPDATE study SET study_status = 'FINISHED' and deleted_at = CURRENT_TIMESTAMP
WHERE id IN (
    SELECT id FROM study
    INNER JOIN study_schedule ss on study.id = ss.study_id
    WHERE DATE_ADD(ss.start_date, INTERVAL 1 DAY) = CURDATE() and ss.week_number = duration_weeks
)
