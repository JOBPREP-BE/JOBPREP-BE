package io.dev.jobprep.domain.applicationstatus.domain.entity;

import io.dev.jobprep.domain.applicationstatus.domain.entity.enums.ApplicationProcess;
import io.dev.jobprep.domain.applicationstatus.domain.entity.enums.ApplicationProgress;
import lombok.Getter;

@Getter
public enum InitData {

    GOOGLE("구글", "SW개발", ApplicationProgress.NOT_STARTED, ApplicationProcess.DOCUMENT_SCREENING, "www.recruitLinkOfSomeCompany.com", null),
    MICROSOFT("마이크로소프트", "재무", ApplicationProgress.IN_PROGRESS, ApplicationProcess.APTITUDE_CODING_TEST, null, null),
    APPLE("애플", "퍼포먼스 마케팅", ApplicationProgress.SUCCEED, ApplicationProcess.FIRST_INTERVIEW, null, null),
    META("메타", "영업 관리", ApplicationProgress.FAILED, ApplicationProcess.FINAL_INTERVIEW, null, null),
    ;

    private final String company;
    private final String position;
    private final ApplicationProgress progress;
    private final ApplicationProcess process;
    private final String url;
    private final String coverLetter;

    InitData(String company, String position, ApplicationProgress progress, ApplicationProcess process, String url, String coverLetter) {
        this.company = company;
        this.position = position;
        this.progress = progress;
        this.process = process;
        this.url = url;
        this.coverLetter = handleCoverLetter(coverLetter);
    }

    private String handleCoverLetter(String coverLetter) {
        if (this.company.equals("구글")) {
            return build();
        }
        return coverLetter;
    }

    private String build() {

        StringBuilder builder = new StringBuilder();
        builder.append("문항 1. 당사에 지원한 동기와 지원 직무에 선택한 이유에 대해 말씀해주세요.\nn");
        builder.append("저는 완전 좋은 사람입니다. 그래서 뽑혀야 합니다.\n");
        builder.append("근데 알고보면 진짜 더 좋은 사람입니다. 그래서 뽑아야 합니다.\n\n");
        builder.append("진짜입니다.\n");
        return builder.toString();
    }
}
