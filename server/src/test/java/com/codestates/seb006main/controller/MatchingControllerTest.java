package com.codestates.seb006main.controller;

import com.codestates.seb006main.Image.service.ImageService;
import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.matching.controller.MatchingController;
import com.codestates.seb006main.matching.dto.MatchingDto;
import com.codestates.seb006main.matching.entity.Matching;
import com.codestates.seb006main.matching.service.MatchingService;
import com.codestates.seb006main.members.dto.MemberDto;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.posts.dto.PostsDto;
import com.codestates.seb006main.posts.entity.Posts;
import com.codestates.seb006main.util.Period;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchingController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class MatchingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MatchingService matchingService;
    @MockBean
    private PrincipalDetails principalDetails;
    @Autowired
    private Gson gson;

    @Test
    public void postMatching() throws Exception {
        //given
        long postId = 1L;

        MatchingDto.Post postDto = MatchingDto.Post.builder()
                .body("테스트")
                .build();
        String content = gson.toJson(postDto);

        MatchingDto.Response responseDto = MatchingDto.Response.builder()
                .matchingId(1L)
                .body("테스트")
                .member(Member.builder().memberId(1L).displayName("회원 이름").build())
                .posts(Posts.builder().postId(1L).title("게시글 제목").build())
                .matchingStatus(Matching.MatchingStatus.NOT_READ)
                .createdAt(LocalDateTime.now())
                .build();

        //mock
        given(matchingService.createMatching(Mockito.anyLong(), Mockito.any(), Mockito.any(Authentication.class))).willReturn(responseDto);

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/matching/posts/{post-id}", postId)
                        .with(csrf())
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.matchingId").value(responseDto.getMatchingId()))
                .andExpect(jsonPath("$.postId").value(postId))
                .andDo(document(
                        "post-matching",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("신청자의 짧은 소개")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("matchingId").type(JsonFieldType.NUMBER).description("매칭 식별자"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("짧은 소개"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("신청자 식별자"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("신청자 이름"),
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                        fieldWithPath("postTitle").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("matchingStatus").type(JsonFieldType.STRING).description("매칭 상태 " +
                                                "(NOT_READ: 읽지 않음 / READ: 읽음(리더가) / ACCEPTED: 수락됨 / REFUSED: 거절됨)"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성한 날짜")
                                )
                        )
                ));
    }

    @Test
    public void getMatching() throws Exception {
        //given
        long matchingId = 1L;

        MatchingDto.Response responseDto = MatchingDto.Response.builder()
                .matchingId(1L)
                .body("")
                .member(Member.builder().memberId(1L).displayName("회원 이름").build())
                .posts(Posts.builder().postId(1L).title("게시글 제목").build())
                .matchingStatus(Matching.MatchingStatus.NOT_READ)
                .createdAt(LocalDateTime.now())
                .build();

        //mock
        given(matchingService.readMatching(Mockito.anyLong(), Mockito.any(Authentication.class))).willReturn(responseDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/matching/{matching-id}", matchingId)
                        .with(csrf())
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchingId").value(matchingId))
                .andDo(document(
                        "get-matching",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("matching-id").description("매칭 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("matchingId").type(JsonFieldType.NUMBER).description("매칭 식별자"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("짧은 소개"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("신청자 식별자"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("신청자 이름"),
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                        fieldWithPath("postTitle").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("matchingStatus").type(JsonFieldType.STRING).description("매칭 상태 " +
                                                "(NOT_READ: 읽지 않음 / READ: 읽음(리더가) / ACCEPTED: 수락됨 / REFUSED: 거절됨)"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성한 날짜")
                                )
                        )
                ));
    }

    @Test
    public void acceptMatching() throws Exception {
        //given
        long matchingId = 1L;

        MatchingDto.Response responseDto = MatchingDto.Response.builder()
                .matchingId(1L)
                .body("")
                .member(Member.builder().memberId(1L).displayName("회원 이름").build())
                .posts(Posts.builder().postId(1L).title("게시글 제목").build())
                .matchingStatus(Matching.MatchingStatus.ACCEPTED)
                .createdAt(LocalDateTime.now())
                .build();

        //mock
        given(matchingService.acceptMatching(Mockito.anyLong(), Mockito.any(Authentication.class))).willReturn(responseDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/matching/{matching-id}/accept", matchingId)
                        .with(csrf())
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchingStatus").value("ACCEPTED"))
                .andDo(document("accept-matching",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("matching-id").description("매칭 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("matchingId").type(JsonFieldType.NUMBER).description("매칭 식별자"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("짧은 소개"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("신청자 식별자"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("신청자 이름"),
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                        fieldWithPath("postTitle").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("matchingStatus").type(JsonFieldType.STRING).description("매칭 상태 " +
                                                "(NOT_READ: 읽지 않음 / READ: 읽음(리더가) / ACCEPTED: 수락됨 / REFUSED: 거절됨)"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성한 날짜")
                                )
                        )
                ));
    }

    @Test
    public void refuseMatching() throws Exception {
        //given
        long matchingId = 1L;

        MatchingDto.Response responseDto = MatchingDto.Response.builder()
                .matchingId(1L)
                .body("")
                .member(Member.builder().memberId(1L).displayName("회원 이름").build())
                .posts(Posts.builder().postId(1L).title("게시글 제목").build())
                .matchingStatus(Matching.MatchingStatus.REFUSED)
                .createdAt(LocalDateTime.now())
                .build();

        //mock
        given(matchingService.refuseMatching(Mockito.anyLong(), Mockito.any(Authentication.class))).willReturn(responseDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/matching/{matching-id}/refuse", matchingId)
                        .with(csrf())
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchingStatus").value("REFUSED"))
                .andDo(document("refuse-matching",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("matching-id").description("매칭 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("matchingId").type(JsonFieldType.NUMBER).description("매칭 식별자"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("짧은 소개"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("신청자 식별자"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("신청자 이름"),
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                        fieldWithPath("postTitle").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("matchingStatus").type(JsonFieldType.STRING).description("매칭 상태 " +
                                                "(NOT_READ: 읽지 않음 / READ: 읽음(리더가) / ACCEPTED: 수락됨 / REFUSED: 거절됨)"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성한 날짜")
                                )
                        )
                ));
    }

    @Test
    public void deleteMatching() throws Exception {
        //given
        long matchingId = 1L;

        doNothing().when(matchingService).cancelMatching(Mockito.anyLong(), Mockito.any(Authentication.class));

        //when
        ResultActions actions = mockMvc.perform(
                delete("/api/matching/{matching-id}", matchingId)
                        .with(csrf())
                        .with(user(principalDetails))
        );

        //then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-matching",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("matching-id").description("매칭 식별자")
                        )
                ));
    }
}
