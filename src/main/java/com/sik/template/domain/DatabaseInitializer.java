package com.sik.template.domain;

import com.github.javafaker.Faker;
import com.sik.template.domain.entity.Account;
import com.sik.template.domain.entity.Board;
import com.sik.template.domain.entity.BoardComment;
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
import java.util.Random;

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

        List<Account> accounts = new ArrayList<>();
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
            accounts.add(account);
        }


        // Insert Board, Comments
        Random rand = new Random();
        for(int i=0; i<10; i++) {
            int accountIdx = rand.nextInt(accounts.size());
            Board board = Board.builder()
                    .title(faker.name().title())
                    .content(faker.book().genre())
                    .build();

            board.setCreatedBy(accounts.get(accountIdx).getUsername());
            board.setLastModifiedBy(accounts.get(accountIdx).getUsername());
            boardRepository.save(board);

            int commentCnt = rand.nextInt(3);
            for(int j=0; j<commentCnt; j++) {
                accountIdx = rand.nextInt(accounts.size());
                BoardComment boardComment = BoardComment.builder()
                        .board(board)
                        .title(faker.name().title())
                        .content(faker.book().title())
                        .build();

                boardComment.setCreatedBy(accounts.get(accountIdx).getUsername());
                boardComment.setLastModifiedBy(accounts.get(accountIdx).getUsername());

                boardCommentRepository.save(boardComment);
            }


        }
    }
}
