package com.sik.template.domain;

import com.github.javafaker.Faker;
import com.sik.template.domain.entity.Account;
import com.sik.template.domain.entity.Role;
import com.sik.template.domain.repository.AccountRepository;
import com.sik.template.domain.repository.BoardCommentRepository;
import com.sik.template.domain.repository.BoardRepository;
import com.sik.template.domain.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Profile("local | test")
public class DatabaseInitializer {

    AccountRepository accountRepository;
    RoleRepository roleRepository;

    BoardRepository boardRepository;
    BoardCommentRepository boardCommentRepository;

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        Faker faker = new Faker();

        // Insert Account & Role
        Role role1 = new Role("API_CALL");
        Role role2 = new Role("ADMIN");

        role1.setCreatedBy("DBA");
        role1.setLastModifiedBy("DBA");
        role2.setCreatedBy("DBA");
        role2.setLastModifiedBy("DBA");

        roleRepository.save(role1);
        roleRepository.save(role2);

        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        roles.add(role2);

        for(int i=0; i<10; i++) {
            String username = i==0 ? "sik" : faker.name().fullName().toLowerCase().replace(" ", "");

            Account account = Account.builder()
                    .username(username)
                    .password("1234")
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .roles(roles)
                    .build();
            account.setCreatedBy("DBA");
            account.setLastModifiedBy("DBA");

            accountRepository.save(account);
        }


    }
}
