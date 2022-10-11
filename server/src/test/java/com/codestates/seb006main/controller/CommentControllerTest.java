package com.codestates.seb006main.controller;

import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.comment.controller.CommentController;
import com.codestates.seb006main.comment.dto.CommentDto;
import com.codestates.seb006main.comment.service.CommentService;
import com.codestates.seb006main.dto.MultiResponseDto;
import com.codestates.seb006main.feed.dto.FeedDto;
import com.codestates.seb006main.feed.entity.Feed;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.posts.controller.PostsController;
import com.codestates.seb006main.posts.service.PostsService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommentService commentService;
    @MockBean
    private PrincipalDetails principalDetails;
    @Autowired
    private Gson gson;

    @Test
    public void postCommentTest() throws Exception {
        //given
        CommentDto.Post postDto = CommentDto.Post.builder()
                .body("정말 좋아보이네요 저도 가고싶은 곳입니다.")
                .feedId(1L)
                .build();
        String content = gson.toJson(postDto);

        CommentDto.Response responseDto = CommentDto.Response.builder()
                .commentId(1L)
                .body("정말 좋아보이네요 저도 가고싶은 곳입니다.")
                .member(Member.builder().memberId(1L).displayName("테스트").profileImage("").build())
                .feed(Feed.builder().feedId(1L).build())
                .createdAt(LocalDateTime.now())
                .modifiedAt(null)
                .build();

        //mock
        given(commentService.createComment(Mockito.any(CommentDto.Post.class), Mockito.any(Authentication.class))).willReturn(responseDto);

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/comments")
                        .with(csrf())
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.body").value(postDto.getBody()))
                .andDo(document(
                        "post-comment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("feedId").type(JsonFieldType.NUMBER).description("피드 식별자")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("댓글 식별자"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("작성자 이름"),
                                        fieldWithPath("feedId").type(JsonFieldType.NUMBER).description("피드 식별자"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성한 날짜"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("마지막으로 수정한 날짜").optional()
                                )
                        )
                ));
    }

    @Test
    public void patchCommentTest() throws Exception {
        //given
        long commentId = 1L;
        CommentDto.Patch patchDto = CommentDto.Patch.builder()
                .body("정말 좋아보이는 여행지네요. 저도 가보고 싶습니다.")
                .build();
        String content = gson.toJson(patchDto);

        CommentDto.Response responseDto = CommentDto.Response.builder()
                .commentId(1L)
                .body("정말 좋아보이는 여행지네요. 저도 가보고 싶습니다.")
                .member(Member.builder().memberId(1L).displayName("테스트").profileImage("").build())
                .feed(Feed.builder().feedId(1L).build())
                .createdAt(LocalDateTime.now().minusHours(1))
                .modifiedAt(LocalDateTime.now())
                .build();

        //mock
        given(commentService.updateComment(Mockito.anyLong(), Mockito.any(CommentDto.Patch.class), Mockito.any(Authentication.class))).willReturn(responseDto);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/api/comments/{comment-id}", commentId)
                        .with(csrf())
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value(patchDto.getBody()))
                .andDo(document(
                        "patch-comment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("comment-id").description("댓글 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("댓글 내용")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("댓글 식별자"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("작성자 이름"),
                                        fieldWithPath("feedId").type(JsonFieldType.NUMBER).description("피드 식별자"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성한 날짜"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("마지막으로 수정한 날짜").optional()
                                )
                        )
                ));
    }

    @Test
    public void getCommentTest() throws Exception {
        //given
        long commentId = 1L;

        CommentDto.Response responseDto = CommentDto.Response.builder()
                .commentId(1L)
                .body("정말 좋아보이네요 저도 가고싶은 곳입니다.")
                .member(Member.builder().memberId(1L).displayName("테스트").profileImage("").build())
                .feed(Feed.builder().feedId(1L).build())
                .createdAt(LocalDateTime.now())
                .modifiedAt(null)
                .build();

        //mock
        given(commentService.readComment(Mockito.anyLong())).willReturn(responseDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/comments/{comment-id}", commentId)
                        .with(csrf())
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "get-comment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("comment-id").description("댓글 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("댓글 식별자"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("작성자 이름"),
                                        fieldWithPath("feedId").type(JsonFieldType.NUMBER).description("피드 식별자"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성한 날짜"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("마지막으로 수정한 날짜").optional()
                                )
                        )
                ));
    }

    @Test
    public void deleteComment() throws Exception {
        //given
        long commentId = 1L;

        doNothing().when(commentService).deleteComment(Mockito.anyLong(), Mockito.any(Authentication.class));

        //when
        ResultActions actions = mockMvc.perform(
                delete("/api/comments/{comment-id}", commentId)
                        .with(csrf())
                        .with(user(principalDetails))
        );

        //then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-comment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("comment-id").description("댓글 식별자")
                        )
                ));
    }
}