package io.dev.jobprep.domain.essentialMaterial.domain;


import io.dev.jobprep.domain.users.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="essential_material")
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EssentialMaterial {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="essential_material")
    private String essentialMaterial;

    public String update(String content){
        this.essentialMaterial = content;
        return this.essentialMaterial;
    }

}
