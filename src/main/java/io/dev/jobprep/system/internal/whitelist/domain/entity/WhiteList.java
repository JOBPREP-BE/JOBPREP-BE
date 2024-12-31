package io.dev.jobprep.system.internal.whitelist.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "white_list")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WhiteList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "access_ip")
    private String accessIp;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    private WhiteList(Long id, String accessIp, LocalDateTime updatedAt) {
        this.id = id;
        this.accessIp = accessIp;
        this.updatedAt = LocalDateTime.now();
    }

    public static WhiteList of(String accessIp) {
        return WhiteList.builder()
            .accessIp(accessIp)
            .build();
    }

}
