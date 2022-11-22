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
@Table(name = "ACCOUNT")
public class Account extends BaseAuditTimeEntity {

    @Id
    @Column(name = "USERNAME")
    private String username;

    @Column
    private String password;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ACCOUNTS_ROLES",
            joinColumns = @JoinColumn(name = "USERNAME"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private List<Role> roles;
}
