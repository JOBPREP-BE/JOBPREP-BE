package io.dev.jobprep.users.domain;

import io.dev.jobprep.common.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserRole userRole = UserRole.NORMAL;

    @Column(name="penalty_updated_at")
    private LocalDateTime penaltyUpdatedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    public User(Long id,
                String username,
                String email,
                UserRole userRole,
                LocalDateTime penaltyUpdatedAt,
                LocalDateTime createdAt,
                LocalDateTime deletedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userRole = userRole;
        this.penaltyUpdatedAt = penaltyUpdatedAt;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public LocalDateTime setDeletedAt() {
        LocalDateTime now = LocalDateTime.now();
        this.deletedAt = LocalDateTime.now(); // 현재 시간을 설정
        return now;
    }

    public void unSetDeletedAt() {
        this.deletedAt = null;
    }

}

