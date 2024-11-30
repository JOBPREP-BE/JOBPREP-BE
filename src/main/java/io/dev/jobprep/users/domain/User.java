package io.dev.jobprep.users.domain;

import io.dev.jobprep.common.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserRole userRole = UserRole.NORMAL;

    @Column(name="penalty_updated_at")
    private LocalDateTime penaltyUpdatedAt;


    public User(Long id,
                String username,
                String email,
                UserRole userRole,
                LocalDateTime penaltyUpdatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userRole = userRole;
        this.penaltyUpdatedAt = penaltyUpdatedAt;
    }
}

