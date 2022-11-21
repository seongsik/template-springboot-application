package com.sik.template.biz.api.account.controller;

import com.sik.template.biz.api.account.dto.AccountDTO;
import com.sik.template.biz.api.account.dto.SignDTO;
import com.sik.template.biz.api.account.service.AccountService;
import com.sik.template.biz.api.base.response.RestApiResponse;
import com.sik.template.biz.api.base.vo.BaseVO;
import com.sik.template.biz.api.board.dto.BoardDTO;
import com.sik.template.biz.api.board.vo.BoardVO;
import com.sik.template.common.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/account")
public class AccountController {

    private AccountService accountService;

    private JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/signin")
    @ResponseBody
    public RestApiResponse<BaseVO, SignDTO> signIn(HttpServletRequest request, @RequestBody AccountDTO accountDTO) {
        AccountDTO loginUser = (AccountDTO) accountService.loadUserByUsername(accountDTO.getUsername());
        SignDTO signDTO = new SignDTO();

        /*
        * Password 검증 수행...
        * */

        List<String> roleList = loginUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        String token = jwtTokenProvider.createToken(loginUser.getUsername(), roleList);
        signDTO.setResult("SUCCESS");
        signDTO.setToken(token);

        RestApiResponse<BaseVO, SignDTO> res = new RestApiResponse<>();
        res.setData(signDTO);
        return res;
    }
}
