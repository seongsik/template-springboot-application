package com.sik.template.domain.entity;

import com.sik.template.domain.base.BaseAuditTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ROLE")
public class Role extends BaseAuditTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long roleId;

    @Column(name = "ROLL_NAME")
    private String roleName;

    @ManyToMany
    @JoinTable(name = "ACCOUNTS_ROLES",
            joinColumns = @JoinColumn(name = "ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "USERNAME")
    )
    private List<Account> accounts;

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
