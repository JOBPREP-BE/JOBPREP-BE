ALTER TABLE application_status
ALTER COLUMN application_progress DROP DEFAULT;

ALTER TABLE application_status
ALTER COLUMN application_process DROP DEFAULT;
