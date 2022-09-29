package com.codestates.seb006main.controller;

import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.members.controller.MemberController;
import com.codestates.seb006main.members.dto.BookmarkDto;
import com.codestates.seb006main.members.dto.MemberDto;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.members.mapper.MemberMapper;
import com.codestates.seb006main.members.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberMapper memberMapper;
    @MockBean
    private MemberService memberService;
    @Autowired
    private Gson gson;
    @MockBean
    private PrincipalDetails principalDetails;

    @MockBean
    private Authentication authentication;

    @Test
    public void postMemberTest() throws Exception {
        //given
        MemberDto.Post post = new MemberDto.Post("rlghd@gmail.com", "132357", "kihong");
        String content = gson.toJson(post);

        MemberDto.Response response = MemberDto.Response.builder()
                .memberId(1L)
                .email("rlghd@gmail.com")
                .displayName("kihong")
                .phone("").content("")
                .memberStatus(Member.MemberStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .profileImage("")
                .role(Member.Role.ROLE_MEMBER)
                .build();

        //Mock
        given(memberService.joinMember(Mockito.any(MemberDto.Post.class))).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/members")
                        .with(csrf())
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );
        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(post.getEmail()))
                .andExpect(jsonPath("$.displayName").value(post.getDisplayName()))
                .andDo(document(
                        "post-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("displayName").type(JsonFieldType.STRING).description("닉네임")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("아이디"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("displayName").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("소개"),
                                        fieldWithPath("memberStatus").type(JsonFieldType.STRING).description("회원 상태"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("계정 생성일"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("계정 수정일").optional(),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("프로필 이미지"),
                                        fieldWithPath("role").type(JsonFieldType.STRING).description("회원권한")
                                )
                        )
                ));
    }

    @Test
    public void  loginMemberTest() throws Exception {
        //given
        MemberDto.Login login = new MemberDto.Login("rlghd@gmail.com","123456");
        String content = gson.toJson(login);
        MemberDto.Response response = MemberDto.Response.builder()
                .memberId(1L)
                .email("rlghd@gmail.com")
                .displayName("kihong")
                .phone("").content("")
                .memberStatus(Member.MemberStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .profileImage("")
                .role(Member.Role.ROLE_MEMBER)
                .build();

        //mock
        given(memberService.loginMember(Mockito.any(Authentication.class))).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/members/login")
                        .with(csrf())
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(login.getEmail()))
                .andDo(document(
                        "post-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("아이디"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("displayName").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("소개"),
                                        fieldWithPath("memberStatus").type(JsonFieldType.STRING).description("회원 상태"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("계정 생성일"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("계정 수정일").optional(),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("프로필 이미지"),
                                        fieldWithPath("role").type(JsonFieldType.STRING).description("회원권한")
                                )
                        )
                ));
    }
    @Test
    public void patchMemberTest() throws Exception {
        //given
        long memberId = 1L;
        MemberDto.Patch patch = new MemberDto.Patch("kkhong", "123456", "01012349876", "","");
        String content = gson.toJson(patch);
        MemberDto.Response response = MemberDto.Response.builder()
                .memberId(1L)
                .email("rlghd@gmail.com")
                .displayName("kkhong")
                .phone("01012349876").content("")
                .memberStatus(Member.MemberStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .profileImage("")
                .role(Member.Role.ROLE_MEMBER)
                .build();

        //Mock
        given(memberService.modifyMember(Mockito.any(MemberDto.Patch.class),Mockito.any(Authentication.class))).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/api/members/")
                        .with(csrf())
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );
        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value(patch.getDisplayName()))
                .andExpect(jsonPath("$.phone").value(patch.getPhone()))
                .andExpect(jsonPath("$.content").value(patch.getContent()))
                .andExpect(jsonPath("$.profileImage").value(patch.getProfileImage()))
                .andDo(document(
                        "patch-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("displayName").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대전화 번호"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("자기소개"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("이미지 url")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("아이디"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("displayName").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("소개"),
                                        fieldWithPath("memberStatus").type(JsonFieldType.STRING).description("회원 상태"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("계정 생성일"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("계정 수정일").optional(),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("프로필 이미지"),
                                        fieldWithPath("role").type(JsonFieldType.STRING).description("회원권한")
                                )
                        )
                ));
    }

    @Test
    public void checkEmailTest() throws Exception {
        //given
        String email = "rlghd@gmail.com";
        String code = "123456";
        Map<String,String> map = new HashMap<>();
        map.put(code,code);
        String content = gson.toJson(map);


        //Mock
        given(memberService.authenticateEmail(Mockito.anyString())).willReturn(code);

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/members/email?email=rlghd@gmail.com")
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        actions
                .andExpect(status().isOk())

                .andDo(document(
                        "get-member-email",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("email").description("이메일")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("인증코드")
                        )

                ));
    }

    @Test
    public void checkDisplayNameTest() throws Exception {
        //given
        String display_name = "kkhong";

        doNothing().when(memberService).verifyExistDisplayName(display_name);

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/members/display-name?display_name="+display_name)
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        actions
                .andExpect(status().isOk())

                .andDo(document(
                        "get-member-display-name",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("display_name").description("닉네임")
                        )
                ));
    }

    @Test
    public void getMemberTest() throws Exception {
        //given
        long memberId = 1L;
        MemberDto.Response response = MemberDto.Response.builder()
                .memberId(1L)
                .email("rlghd@gmail.com")
                .displayName("kkhong")
                .phone("01012349876").content("")
                .memberStatus(Member.MemberStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .profileImage("")
                .role(Member.Role.ROLE_MEMBER)
                .build();
        given(memberService.findMember(Mockito.anyLong())).willReturn(response);


        //when
        ResultActions actions = mockMvc.perform(
                get("/api/members/{member-id}",memberId)
                        .with(csrf())
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())

                .andDo(document(
                        "get-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("member-id").description("아이디")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("아이디"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("displayName").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("소개"),
                                        fieldWithPath("memberStatus").type(JsonFieldType.STRING).description("회원 상태"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("계정 생성일"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("계정 수정일").optional(),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("프로필 이미지"),
                                        fieldWithPath("role").type(JsonFieldType.STRING).description("회원권한")
                                )
                        )
                ));
    }

    @Test
    public void deleteMemberTest() throws Exception {
        //given
        String password = "1234566";
        Map<String,String> map = Map.of("password",password);
        String content = gson.toJson(map);
        System.out.println(content);
        doNothing().when(memberService).withdrawalMember("123456",authentication);

        //when
        ResultActions actions = mockMvc.perform(
                delete("/api/members")
                        .with(csrf())
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        actions
                .andExpect(status().isNoContent())

                .andDo(document(
                        "delete-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    public void bookmarkTest() throws Exception {
        //given
        Long postId = 1L;

        doNothing().when(memberService).changeBookmark(postId,authentication);

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/members/bookmark?postId="+postId)
                        .with(csrf())
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "get-bookmark",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    public void myBookmarkTest() throws Exception {
        //given

        List<Long> responses = new ArrayList<>();
        responses.add(1L);
        responses.add(2L);
        Map<String,List<Long>> response=Map.of("postIds",responses);
        given(memberService.findMyBookmark(Mockito.any(Authentication.class))).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/members/my-bookmark")
                        .with(csrf())
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "get-my-bookmarks",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                responseFields(

                                fieldWithPath("postIds").type(JsonFieldType.ARRAY).description("게시물 아이디")

                )));

    }

}
