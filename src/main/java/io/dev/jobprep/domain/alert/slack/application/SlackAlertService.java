package io.dev.jobprep.domain.alert.slack.application;

import static com.slack.api.model.block.Blocks.asBlocks;
import static com.slack.api.model.block.Blocks.divider;
import static com.slack.api.model.block.Blocks.header;
import static com.slack.api.model.block.Blocks.section;
import static io.dev.jobprep.exception.code.ErrorCode400.SLACK_ALERT_FAILURE;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.composition.TextObject;
import io.dev.jobprep.domain.alert.exception.AlertException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackAlertService {

    private static final Integer MAX_RETRY_ATTEMTPS = 5;
    private static final String RECOMMEND_MSG = "Please Check server-log and Resolve err!";

    @Value("${slack.bot-token}")
    private String token;

    @Value("${slack.channel.monitor}")
    private String channel;

    @Value("${app.version}")
    private String appVersion;

    /**
     Slack 알림
     - 작업 이름: 실패한 sql 파일명 예) delete_study_for_3_duration_weeks_after_12
     - 실행 시간: 예) 2024.12.22T01:12:48.000Z
     - 예외 메시지: 발생한 예외의 상세 메시지 예) java.net.ConnectException: Connection timed out.
     - 예외 유형: 예외 클래스 이름 예) ConnectException
     - 스택 트레이스: e.printStackTrace()
     - 재시도 횟수: 5
     - 호스트: Host: app-dev-server (34.47.100.172)
     - 애플리케이션 버전: Version:
     - 다음 작업 예상 시간: Next Scheduled Attempt: 2024-12-23T24:00:00
     - 후속 작업 권장: Please restart the scheduler or check the server logs!
     */

    public void sendAlertMsgToSlack(
        String scheduler,
        Exception e
    ) {

        List<TextObject> objects = postProcess(scheduler, e);
        MethodsClient methodClient = Slack.getInstance().methods(token);
        ChatPostMessageRequest request = generateRequest(objects, e);

        try {
            methodClient.chatPostMessage(request);
            log.info("send alert message successfully for slack channel {}", request.getChannel());
        } catch (Exception e_) {
            log.error("Failed to send message to Slack cause {} for exception {}", e_.getMessage(), e.getMessage());
            throw new AlertException(SLACK_ALERT_FAILURE);
        }
    }

    private List<TextObject> postProcess(String scheduler, Exception e) {
        List<TextObject> objects = new ArrayList<>();
        objects.add(MarkdownTextObject.builder().text("*Job Name:*\n" + scheduler).build());
        objects.add(MarkdownTextObject.builder().text("*Failure Point:*\n" + LocalDateTime.now()).build());
        objects.add(MarkdownTextObject.builder().text("*Error Msg:*\n" + e.getMessage()).build());
        objects.add(MarkdownTextObject.builder().text("*Exception Type:*\n" + e.getClass().getName()).build());
        objects.add(MarkdownTextObject.builder().text("*Recommended Follow-up Actions:*\n" + RECOMMEND_MSG).build());
        return objects;
    }

    private ChatPostMessageRequest processRequest(List<TextObject> objects, Exception e) {
        return ChatPostMessageRequest.builder()
            .channel(channel)
            .text(e.getMessage())
            .blocks(asBlocks(
                header(header -> header.text(PlainTextObject.builder().text("System Failure Report").build())),
                divider(),
                section(section -> section.fields(objects))
            ))
            .build();
    }

    @Deprecated
    private List<TextObject> process(
        String scheduler,
        Exception e
    ) {
        List<TextObject> objects = new ArrayList<>();
        objects.add(MarkdownTextObject.builder().text("*Job Name:*\n" + scheduler).build());
        objects.add(MarkdownTextObject.builder().text("*Failure Point:*\n" + LocalDateTime.now()).build());
        objects.add(MarkdownTextObject.builder().text("*Error Msg:*\n" + e.getMessage()).build());
        objects.add(MarkdownTextObject.builder().text("*Exception Type:*\n" + e.getClass().getName()).build());
        objects.add(MarkdownTextObject.builder().text("*StackTrace:*\n" + parse(e)).build());
        objects.add(MarkdownTextObject.builder().text("*Retry Attempts:*\n" + MAX_RETRY_ATTEMTPS).build());
        objects.add(MarkdownTextObject.builder().text("*Host:*\n" + getHostInfo()).build());
        objects.add(MarkdownTextObject.builder().text("*App Version:*\n" + appVersion).build());
        objects.add(MarkdownTextObject.builder().text("*Next Scheduled Attempt:*\n" + calculateNextScheduledAttemps()).build());
        objects.add(MarkdownTextObject.builder().text("*Recommended Follow-up Actions:*\n" + RECOMMEND_MSG).build());
        return objects;
    }

    @Deprecated
    private String parse(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    @Deprecated
    private String getHostInfo() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Unknown Host";
        }
    }

    @Deprecated
    private LocalDateTime calculateNextScheduledAttemps() {
        LocalDateTime today = LocalDateTime.now();
        return today.plusDays(1).toLocalDate().atTime(LocalTime.MIDNIGHT);
    }

    @Deprecated
    private ChatPostMessageRequest generateRequest(List<TextObject> objects, Exception e) {
        return ChatPostMessageRequest.builder()
            .channel(channel)
            .text(e.getMessage())
            .blocks(asBlocks(
                header(header -> header.text(PlainTextObject.builder().text("System Failure Report").build())),
                divider(),
                section(section -> section.fields(objects))
            ))
            .build();
    }

}
