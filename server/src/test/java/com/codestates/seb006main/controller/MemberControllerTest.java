package com.codestates.seb006main.controller;

import com.codestates.seb006main.members.controller.MemberController;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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

    @Test
    public void postMemberTest() throws Exception {
        //given
        MemberDto.Post post = new MemberDto.Post("rlghd@gmail.com", "132357", "kihong");
        String content = gson.toJson(post);

        MemberDto.Response response = MemberDto.Response.builder()
                .memberId(1L)
                .email("rlghd@gmail.com")
                .display_name("kihong")
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
                patch("/api/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );
        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(post.getEmail()))
                .andExpect(jsonPath("$.display_name").value(post.getDisplay_name()))
                .andDo(document(
                        "post-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("display_name").type(JsonFieldType.STRING).description("닉네임")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("아이디"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("display_name").type(JsonFieldType.STRING).description("닉네임"),
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
        System.out.println(content);
        MemberDto.Response response = MemberDto.Response.builder()
                .memberId(1L)
                .email("rlghd@gmail.com")
                .display_name("kkhong")
                .phone("01012349876").content("")
                .memberStatus(Member.MemberStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .profileImage("")
                .role(Member.Role.ROLE_MEMBER)
                .build();

        //Mock
        given(memberService.modifyMember(Mockito.any(MemberDto.Patch.class),Mockito.anyLong())).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/api/members/{member-id}", memberId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );
        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.display_name").value(patch.getDisplay_name()))
                .andExpect(jsonPath("$.phone").value(patch.getPhone()))
                .andExpect(jsonPath("$.content").value(patch.getContent()))
                .andExpect(jsonPath("$.profileImage").value(patch.getProfileImage()))
                .andDo(document(
                        "post-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("display_name").type(JsonFieldType.STRING).description("닉네임"),
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
                                        fieldWithPath("display_name").type(JsonFieldType.STRING).description("닉네임"),
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
}
