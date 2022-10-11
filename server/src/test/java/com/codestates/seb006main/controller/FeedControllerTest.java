package com.codestates.seb006main.controller;

import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.dto.MultiResponseDto;
import com.codestates.seb006main.feed.controller.FeedController;
import com.codestates.seb006main.feed.dto.FeedDto;
import com.codestates.seb006main.feed.entity.Feed;
import com.codestates.seb006main.feed.service.FeedService;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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

@WebMvcTest(FeedController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class FeedControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FeedService feedService;
    @MockBean
    private PrincipalDetails principalDetails;
    @Autowired
    private Gson gson;

    @Test
    public void postFeedTest() throws Exception {
        //given
        FeedDto.Post postDto = FeedDto.Post.builder()
                .body("즐거운 여행이었어요 호호호")
                .images(new ArrayList<>())
                .build();
        String content = gson.toJson(postDto);

        FeedDto.Response responseDto = FeedDto.Response.builder()
                .feedId(1L)
                .body("즐거운 여행이었어요 호호호")
                .member(Member.builder().memberId(1L).displayName("테스트").profileImage("").build())
                .createdAt(LocalDateTime.now())
                .modifiedAt(null)
                .comments(new ArrayList<>())
                .build();

        //mock
        given(feedService.createFeed(Mockito.any(FeedDto.Post.class), Mockito.any(Authentication.class))).willReturn(responseDto);

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/feeds")
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
                        "post-feed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("피드 내용"),
                                        fieldWithPath("images").type(JsonFieldType.ARRAY).description("이미지 url 리스트")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("feedId").type(JsonFieldType.NUMBER).description("피드 식별자"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("피드 내용"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("작성자 이름"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("프로필 이미지 경로"),
                                        fieldWithPath("comments").type(JsonFieldType.ARRAY).description("댓글 목록"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성한 날짜"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("마지막으로 수정한 날짜").optional()
                                )
                        )
                ));
    }

    @Test
    public void patchFeedTest() throws Exception {
        //given
        long feedId = 1L;
        FeedDto.Patch patchDto = FeedDto.Patch.builder()
                .body("즐거운 여행이었어요 호호호 다음에 또 가고 싶네요")
                .images(new ArrayList<>())
                .build();
        String content = gson.toJson(patchDto);

        FeedDto.Response responseDto = FeedDto.Response.builder()
                .feedId(1L)
                .body("즐거운 여행이었어요 호호호 다음에 또 가고 싶네요")
                .member(Member.builder().memberId(1L).displayName("테스트").profileImage("").build())
                .createdAt(LocalDateTime.now().minusHours(1))
                .modifiedAt(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        //mock
        given(feedService.updateFeed(Mockito.anyLong(), Mockito.any(FeedDto.Patch.class), Mockito.any(Authentication.class))).willReturn(responseDto);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/api/feeds/{feed-id}", feedId)
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
                        "patch-feed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("feed-id").description("피드 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("피드 내용"),
                                        fieldWithPath("images").type(JsonFieldType.ARRAY).description("이미지 url 리스트")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("feedId").type(JsonFieldType.NUMBER).description("피드 식별자"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("피드 내용"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("작성자 이름"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("프로필 이미지 경로"),
                                        fieldWithPath("comments").type(JsonFieldType.ARRAY).description("댓글 목록"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성한 날짜"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("마지막으로 수정한 날짜").optional()
                                )
                        )
                ));
    }

    @Test
    public void getFeedTest() throws Exception {
        //given
        long feedId = 1L;

        FeedDto.Response responseDto = FeedDto.Response.builder()
                .feedId(1L)
                .body("즐거운 여행이었어요 호호호 다음에 또 가고 싶네요")
                .member(Member.builder().memberId(1L).displayName("테스트").profileImage("").build())
                .createdAt(LocalDateTime.now().minusHours(1))
                .modifiedAt(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        //mock
        given(feedService.readFeed(Mockito.anyLong())).willReturn(responseDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/feeds/{feed-id}", feedId)
                        .with(csrf())
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "get-feed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("feed-id").description("피드 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("feedId").type(JsonFieldType.NUMBER).description("피드 식별자"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("피드 내용"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("작성자 이름"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("프로필 이미지 경로"),
                                        fieldWithPath("comments").type(JsonFieldType.ARRAY).description("댓글 목록"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성한 날짜"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("마지막으로 수정한 날짜").optional()
                                )
                        )
                ));
    }

    @Test
    public void getAllPostsTest() throws Exception {
        //given
        Page<Feed> feedPage = new PageImpl<>(
                List.of(
                        Feed.builder().feedId(1L).build(),
                        Feed.builder().feedId(2L).build()
                ),
                PageRequest.of(0, 10, Sort.by("feedId").descending()), 2);


        FeedDto.Response responseDto1 = FeedDto.Response.builder()
                .feedId(1L)
                .body("즐거운 여행이었어요 호호호 다음에 또 가고 싶네요")
                .member(Member.builder().memberId(1L).displayName("테스트1").profileImage("").build())
                .createdAt(LocalDateTime.now().minusHours(1))
                .modifiedAt(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        FeedDto.Response responseDto2 = FeedDto.Response.builder()
                .feedId(2L)
                .body("이번에 다녀온 여행지입니다 사진 구경하세요")
                .member(Member.builder().memberId(2L).displayName("테스트2").profileImage("").build())
                .createdAt(LocalDateTime.now().minusHours(1))
                .modifiedAt(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        MultiResponseDto responseDto = new MultiResponseDto<>(
                List.of(responseDto2, responseDto1), feedPage);

        //mock
        given(feedService.readAllFeeds(Mockito.any())).willReturn(responseDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/feeds")
                        .param("page", "1")
                        .param("size", "10")
                        .with(csrf())
                        .with(user(principalDetails))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document(
                        "get-all-feeds",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                List.of(
                                        parameterWithName("page").description("Page 번호 (기본값 : 1)"),
                                        parameterWithName("size").description("Page 크기 (기본값 : 10)"),
                                        parameterWithName("_csrf").description("csrf").ignored()
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("게시글 식별자"),
                                        fieldWithPath("data[].feedId").type(JsonFieldType.NUMBER).description("피드 식별자"),
                                        fieldWithPath("data[].body").type(JsonFieldType.STRING).description("피드 내용"),
                                        fieldWithPath("data[].memberId").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                        fieldWithPath("data[].memberName").type(JsonFieldType.STRING).description("작성자 이름"),
                                        fieldWithPath("data[].profileImage").type(JsonFieldType.STRING).description("프로필 이미지 경로"),
                                        fieldWithPath("data[].comments").type(JsonFieldType.ARRAY).description("댓글 목록"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("작성한 날짜"),
                                        fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING).description("마지막으로 수정한 날짜").optional(),
                                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("전체 건 수"),
                                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수")
                                )
                        )
                ));
    }

    @Test
    public void deletePosts() throws Exception {
        //given
        long feedId = 1L;

        doNothing().when(feedService).deleteFeed(Mockito.anyLong(), Mockito.any(Authentication.class));

        //when
        ResultActions actions = mockMvc.perform(
                delete("/api/feeds/{feed-id}", feedId)
                        .with(csrf())
                        .with(user(principalDetails))
        );

        //then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-feed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("feed-id").description("피드 식별자")
                        )
                ));
    }
}
