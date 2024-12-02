package io.dev.jobprep.users.domain;

import io.dev.jobprep.common.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchConnectionDetails;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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

    public LocalDateTime setDeletedAt() {
        super.delete();
        return super.getDeletedAt();
    }

    public void unSetdeleteAt(){
        super.unDelete();
    }

}

