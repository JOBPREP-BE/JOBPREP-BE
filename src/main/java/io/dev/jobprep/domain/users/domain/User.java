package io.dev.jobprep.domain.users.domain;

import io.dev.jobprep.common.base.BaseTimeEntity;
import io.dev.jobprep.domain.users.exception.UserException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static io.dev.jobprep.exception.code.ErrorCode401.USER_ACCOUNT_DISABLED;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    public User(
                String username,
                String email,
                UserRole userRole,
                LocalDateTime penaltyUpdatedAt) {
        this.username = username;
        this.email = email;
        this.userRole = userRole;
        this.penaltyUpdatedAt = penaltyUpdatedAt;
    }

    public void delete() {
        this.validateUserActive();
        super.delete();
    }

    public void restore(){
        super.restore();
    }

    public void validateUserActive() {
        if (super.getDeletedAt() != null) {
            throw new UserException(USER_ACCOUNT_DISABLED);
        }
    }

    public void validateAdmin() {
        if (!this.userRole.equals(UserRole.ADMIN)) {
            throw new UserException(ADMIN_FORBIDDEN_OPERATION);
        }
    }

}

