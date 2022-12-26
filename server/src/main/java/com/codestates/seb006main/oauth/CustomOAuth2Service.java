package com.codestates.seb006main.oauth;

import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class CustomOAuth2Service extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2 로그인 진행 시 키가 되는 필드 값(PK)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        Member member = saveOrUpdate(attributes);
        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }

    private Member saveOrUpdate(OAuthAttributes attributes){
        Member member = memberRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.updateOAuth(attributes.getName(), attributes.getPicture()))
                        .orElse(attributes.toEntity());
        return memberRepository.save(member);
    }

}
