package io.dev.jobprep.scheduler;

import io.dev.jobprep.common.alert.mail.handler.MailHandler;
import io.dev.jobprep.domain.alert.application.AlertReportService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLTimeoutException;
import java.util.stream.Collectors;
import javax.naming.CommunicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class StudyAutoDeletionScheduler {

    private static final String EMPTY = "";
    private static final String LINE_BREAK = "\n";

    private final JdbcTemplate jdbcTemplate;
    private final Resource[] sqlStatements;
    private final AlertReportService alertReportService;
    private final MailHandler mailHandler;

    protected StudyAutoDeletionScheduler(
        JdbcTemplate jdbcTemplate,
        @Qualifier("webApplicationContext") ResourcePatternResolver resourcePatternResolver,
        @Value("${path.schedule.sql}") String sqlStatementsPath,
        AlertReportService alertReportService,
        MailHandler mailHandler
    ) throws IOException {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlStatements = resourcePatternResolver.getResources(sqlStatementsPath);
        this.alertReportService = alertReportService;
        this.mailHandler = mailHandler;
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Transactional(rollbackFor = {DataAccessException.class, Exception.class})
    @Retryable(
        retryFor = {
            DataAccessResourceFailureException.class, QueryTimeoutException.class,
            CannotGetJdbcConnectionException.class, SQLTimeoutException.class,
            CommunicationException.class, DataSourceLookupFailureException.class
        },
        maxAttempts = 5,
        backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void executeSqlFile() {
        for (Resource statement : sqlStatements) {
            execute(statement);
        }
    }

    @Transactional(rollbackFor = {DataAccessException.class, Exception.class})
    public void execute(Resource statement) {
        String sql = loadSqlFromFile(statement);
        try {
            jdbcTemplate.execute(sql);
        } catch (DataAccessResourceFailureException | QueryTimeoutException |
                 DataSourceLookupFailureException e) {
            // do-nothing cause auto-retry!
        } catch (DataAccessException e) {
            log.warn("Failed to executing Sql query cause {} for resource {}",
                e.getMessage(), statement.getFilename());
            report(statement.getFilename(), e);
            throw e;
        } catch (Exception e) {
            log.error("Execution of sql query failed due to an unexpected error {} for resource {}",
                e.getMessage(), statement.getFilename());
            report(statement.getFilename(), e);
            throw e;
        }
    }

    @Recover
    public void fallbackMethod(Exception e) {
        String scheduler = "STUDY-AUTO-DELETION SCHEDULER";
        log.error("sql 스케줄링 실행 과정에서 오류 발생 : " + scheduler, e);
        report(scheduler, e);
    }

    private String loadSqlFromFile(Resource resource) {
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
        )) {
            return reader.lines().collect(Collectors.joining(LINE_BREAK));
        } catch (IOException e) {
            log.error("sql 스케줄링 파일 로드 과정에서 오류 발생 : " + resource.getFilename(), e);
            report(resource.getFilename(), e);
            return EMPTY;
        }
    }

    private void report(String job, Exception e) {
        alertReportService.report(job, e);
        mailHandler.postProcessEmailWithErr(job, e);
    }

    @Deprecated
    @Transactional
    public void deleteStudyForShortOnMember() {
        jdbcTemplate.execute(
            """
            UPDATE study SET study_status = 'RECRUITMENT_CLOSED' and deleted_at = CURRENT_TIMESTAMP
            WHERE id IN (
                SELECT id FROM study
                INNER JOIN user_study us on study.id = us.study_id
                INNER JOIN study_schedule ss on study.id = ss.study_id
                WHERE DATE(ss.start_date) = CURDATE() and ss.week_number = 1
                HAVING COUNT(DISTINCT us.id) < study.head_count
            )
            """
        );
    }

    @Deprecated
    @Transactional
    public void deleteStudyForCompletion() {
        jdbcTemplate.execute(
            """
            UPDATE study SET study_status = 'FINISHED' and deleted_at = CURRENT_TIMESTAMP
            WHERE id IN (
                SELECT id FROM study
                INNER JOIN study_schedule ss on study.id = ss.study_id
                WHERE DATE_ADD(ss.start_date, INTERVAL 1 DAY) = CURDATE() and ss.week_number = duration_weeks
            )
            """
        );
    }


}
