package io.dev.jobprep.common.alert.mail.handler;

import static io.dev.jobprep.common.alert.mail.PrettyPrintHelper.BOLD_END;
import static io.dev.jobprep.common.alert.mail.PrettyPrintHelper.BOLD_STRT;
import static io.dev.jobprep.common.alert.mail.PrettyPrintHelper.EMPTY;
import static io.dev.jobprep.common.alert.mail.PrettyPrintHelper.LIGHT_BOLD_END;
import static io.dev.jobprep.common.alert.mail.PrettyPrintHelper.LIGHT_BOLD_MID;
import static io.dev.jobprep.common.alert.mail.PrettyPrintHelper.LIGHT_BOLD_STRT;
import static io.dev.jobprep.common.alert.mail.PrettyPrintHelper.LINE_BREAK;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailHandler {

    private static final Integer MAX_RETRY_ATTEMTPS = 5;
    private static final String TOPIC = "Scheduler Task Retry Failure Notification";
    private static final String RECOMMEND_MSG = "Please restart the scheduler or check the server logs!";
    private static final String SUBJECT = "Scheduling Task Retry Failure Notification: %s";

    @Value("${spring.mail.username}")
    private String RECIPIENT;

    @Value("${app.version}")
    private String appVersion;

    private final JavaMailSender mailSender;

    public void postProcessEmailWithErr(String jobScheduler, Exception e) {

        String subject = String.format(SUBJECT, jobScheduler);

        // message Processing...
        String content = generateEmailContent(jobScheduler, e);

        try {
            sendMail(subject, content);
            log.info("send alert mail successfully to administraction!");
        } catch (MessagingException | MailException e_) {
            log.warn("Failed to send mail for Scheduler Err Notification: {}", e_.getMessage());
            e.printStackTrace();
        }
    }

    private String generateEmailContent(String jobScheduler, Exception e) {

        StringBuilder builder = new StringBuilder();
        builder.append(makeBold(TOPIC));
        builder.append(makeLightBold("Job Name:", jobScheduler));
        builder.append(makeLightBold("Failure Point:", LocalDateTime.now().toString()));
        builder.append(makeLightBold("Error Msg:", e.getMessage()));
        builder.append(makeLightBold("Exception Type:" , e.getClass().getName()));
        builder.append(makeLightBold("StackTrace:", parse(e)));
        builder.append(makeLightBold("Retry Attempts:", MAX_RETRY_ATTEMTPS.toString()));
        builder.append(makeLightBold("Host:", getHostInfo()));
        builder.append(makeLightBold("App Version:", appVersion));
        builder.append(makeLightBold("Next Scheduled Attempt:", calculateNextScheduledAttemps().toString()));
        builder.append(makeLightBold("Recommended Follow-up Action:", RECOMMEND_MSG));
        return builder.toString();
    }

    private String parse(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    private String getHostInfo() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Unknown Host";
        }
    }

    private LocalDateTime calculateNextScheduledAttemps() {
        LocalDateTime today = LocalDateTime.now();
        return today.plusDays(1).toLocalDate().atTime(LocalTime.MIDNIGHT);
    }

    private String makeBold(String message) {
        return BOLD_STRT + message + BOLD_END + LINE_BREAK;
    }

    private String makeLightBold(String key, String value) {
        return LIGHT_BOLD_STRT + key + LIGHT_BOLD_MID + EMPTY + value + LIGHT_BOLD_END + LINE_BREAK;
    }

    private void sendMail(String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(RECIPIENT);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }
}
