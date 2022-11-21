package com.sik.template.biz.api.account.service.impl;

import com.sik.template.biz.api.account.dto.AccountDTO;
import com.sik.template.biz.api.account.service.AccountService;
import com.sik.template.domain.entity.Account;
import com.sik.template.domain.entity.Role;
import com.sik.template.domain.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username : " + username + " not found"));

        AccountDTO accountDTO = AccountDTO.convertAccount(account);
        accountDTO.setAuthorities((Collection<GrantedAuthority>) getAuthorities(account));

        return accountDTO;
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(Account account) {
        String[] userRoles = account.getRoles().stream().map(Role::getRoleName).toArray(String[]::new);
        return AuthorityUtils.createAuthorityList(userRoles);
    }


}
