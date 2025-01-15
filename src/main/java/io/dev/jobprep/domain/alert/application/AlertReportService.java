package io.dev.jobprep.domain.alert.application;

import io.dev.jobprep.domain.alert.slack.application.SlackAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlertReportService {

    private final SlackAlertService slackAlertService;

    @Transactional
    public void report(String scheduler, Exception e) {
        slackAlertService.sendAlertMsgToSlack(scheduler, e);
    }

}
