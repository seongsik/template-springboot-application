package com.sik.template.biz.api.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sik.template.biz.api.board.dto.BoardDTO;
import com.sik.template.domain.entity.Account;
import com.sik.template.domain.entity.Board;
import com.sik.template.domain.entity.Role;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;

import java.util.Collection;
import java.util.Map;

@Data
public class AccountDTO implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String firstName;
    private String lastName;

    private String email;

    private Collection<GrantedAuthority> authorities;


    public static AccountDTO convertAccount(Account entity) {
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(new PropertyMap<Account, AccountDTO>() {
            @Override
            protected void configure() {
                skip(destination.getAuthorities());
            }
        });

        return mapper.map(entity, AccountDTO.class);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
