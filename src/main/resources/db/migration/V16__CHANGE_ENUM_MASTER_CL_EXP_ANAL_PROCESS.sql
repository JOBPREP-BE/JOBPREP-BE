ALTER TABLE experience_master_cl
MODIFY COLUMN exp_anal_process ENUM('PREPARATION', 'IN_PROGRESS', 'FINALIZED') NOT NULL DEFAULT 'PREPARATION'