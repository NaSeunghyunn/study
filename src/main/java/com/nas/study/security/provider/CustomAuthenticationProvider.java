package com.nas.study.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();

        CustomUserDetail principal = (CustomUserDetail) userDetailsService.loadUserByUsername(email);

        checkPassword(authentication, principal);

        //인증 성공 시 성공한 인증 객체를 생성 후 반환
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal.getMember(), null, principal.getAuthorities());

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 전달된 파라미터의 타입이 UsernamePasswordAuthenticationToken 타입과 일치하는지 검사한다.
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private void checkPassword(Authentication authentication, CustomUserDetail customUserDetail) {
        String credentials = authentication.getCredentials().toString();
        String password = customUserDetail.getPassword();

        if (!passwordEncoder.matches(credentials, password)) {
            throw new BadCredentialsException("BadCredentialsException");
        }

    }
}
