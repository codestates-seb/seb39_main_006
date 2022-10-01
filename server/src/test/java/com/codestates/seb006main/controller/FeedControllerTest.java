package com.codestates.seb006main.controller;

import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.dto.MultiResponseDto;
import com.codestates.seb006main.feed.controller.FeedController;
import com.codestates.seb006main.feed.dto.FeedDto;
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
    public void postPostsTest() throws Exception {
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
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("게시글 내용"),
                                        fieldWithPath("images").type(JsonFieldType.ARRAY).description("이미지 ID 리스트")
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

//    @Test
//    public void patchPostsTest() throws Exception {
//        //given
//        long postId = 1L;
//        PostsDto.Patch patchDto = PostsDto.Patch.builder()
//                .title("(급합니다) 사이판 여행 모집 구해봐요")
//                .body("일정이 촉박해 빠르게 사이판으로 함께 갈 1명만 구해봅니다. 여자 혼자 가기 너무 심심하네요. 흑흑흑.")
//                .totalCount(2)
//                .closeDate("2022-10-12")
//                .images(new ArrayList<>())
//                .build();
//        String content = gson.toJson(patchDto);
//
//        PostsDto.Response responseDto = PostsDto.Response.builder()
//                .postId(1L)
//                .title("(급합니다) 사이판 여행 모집 구해봐요")
//                .body("일정이 촉박해 빠르게 사이판으로 함께 갈 1명만 구해봅니다. 여자 혼자 가기 너무 심심하네요. 흑흑흑.")
//                .member(Member.builder().memberId(1L).displayName("테스트").build())
//                .postsStatus(Posts.PostsStatus.READY)
//                .createdAt(LocalDateTime.of(2022, 9, 17, 12, 0, 0))
//                .modifiedAt(LocalDateTime.now())
//                .travelPeriod(new Period(LocalDate.of(2022, 11, 11),
//                        LocalDate.of(2022, 11, 14)))
//                .location("사이판")
//                .totalCount(2)
//                .closeDate(LocalDate.of(2022, 10, 12))
//                .participants(List.of(MemberDto.Participants.builder()
//                        .memberId(1L)
//                        .displayName("테스트")
//                        .content("자기소개")
//                        .profileImage("")
//                        .build()))
//                .build();
//
//        //mock
//        given(postsService.updatePosts(Mockito.anyLong(), Mockito.any(PostsDto.Patch.class), Mockito.any(Authentication.class))).willReturn(responseDto);
//
//        //when
//        ResultActions actions = mockMvc.perform(
//                patch("/api/posts/{post-id}", postId)
//                        .with(csrf())
//                        .with(user(principalDetails))
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(content)
//        );
//
//        //then
//        actions
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value(patchDto.getTitle()))
//                .andExpect(jsonPath("$.body").value(patchDto.getBody()))
//                .andDo(document(
//                        "patch-posts",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        pathParameters(
//                                parameterWithName("post-id").description("게시글 식별자")
//                        ),
//                        requestFields(
//                                List.of(
//                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
//                                        fieldWithPath("body").type(JsonFieldType.STRING).description("게시글 내용"),
//                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("모집 인원"),
//                                        fieldWithPath("closeDate").type(JsonFieldType.STRING).description("모집 종료 날짜(\"YYYY-MM-DD\")"),
//                                        fieldWithPath("images").type(JsonFieldType.ARRAY).description("이미지 ID 목록")
//                                )
//                        ),
//                        responseFields(
//                                List.of(
//                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
//                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
//                                        fieldWithPath("body").type(JsonFieldType.STRING).description("게시글 내용"),
//                                        fieldWithPath("leaderId").type(JsonFieldType.NUMBER).description("작성자/ 그룹장 식별자"),
//                                        fieldWithPath("leaderName").type(JsonFieldType.STRING).description("작성자/ 그룹장 이름"),
//                                        fieldWithPath("startDate").type(JsonFieldType.STRING).description("여행 시작 날짜(\"YYYY-MM-DD\")"),
//                                        fieldWithPath("endDate").type(JsonFieldType.STRING).description("여행 종료 날짜(\"YYYY-MM-DD\")"),
//                                        fieldWithPath("location").type(JsonFieldType.STRING).description("여행 지역"),
//                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("모집 인원"),
//                                        fieldWithPath("participants").type(JsonFieldType.ARRAY).description("참여 인원 목록"),
//                                        fieldWithPath("participants[].memberId").type(JsonFieldType.NUMBER).description("참여 인원 식별자"),
//                                        fieldWithPath("participants[].displayName").type(JsonFieldType.STRING).description("참여 인원 이름"),
//                                        fieldWithPath("participants[].profileImage").type(JsonFieldType.STRING).description("참여 인원 프로필 이미지 경로"),
//                                        fieldWithPath("participants[].content").type(JsonFieldType.STRING).description("참여 인원 자기소개"),
//                                        fieldWithPath("participantsCount").type(JsonFieldType.NUMBER).description("참여 인원 수"),
//                                        fieldWithPath("closeDate").type(JsonFieldType.STRING).description("모집 종료 날짜(\"YYYY-MM-DD\")"),
//                                        fieldWithPath("postsStatus").type(JsonFieldType.STRING).description("게시글 상태 " +
//                                                "(INACTIVE: 비활성화 / READY: 모집 예정 / RECRUITING: 모집 중 / COMPLETED: 모집 완료)"),
//                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성한 날짜"),
//                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("마지막으로 수정한 날짜")
//                                )
//                        )
//                ));
//    }
//
//    @Test
//    public void getPostsTest() throws Exception {
//        //given
//        long postId = 1L;
//
//        PostsDto.Response responseDto = PostsDto.Response.builder()
//                .postId(1L)
//                .title("사이판 여행 모집")
//                .body("사이판으로 여행가실 여자분 2명 구합니다. 여자 혼자 가기 너무 심심하네요. 호호호.")
//                .member(Member.builder().memberId(1L).displayName("테스트").build())
//                .postsStatus(Posts.PostsStatus.READY)
//                .createdAt(LocalDateTime.of(2022, 9, 17, 12, 0, 0))
//                .modifiedAt(LocalDateTime.now())
//                .travelPeriod(new Period(LocalDate.of(2022, 11, 11),
//                        LocalDate.of(2022, 11, 14)))
//                .location("사이판")
//                .totalCount(3)
//                .closeDate(LocalDate.of(2022, 10, 12))
//                .participants(List.of(MemberDto.Participants.builder()
//                        .memberId(1L)
//                        .displayName("테스트")
//                        .content("자기소개")
//                        .profileImage("")
//                        .build()))
//                .build();
//
//        //mock
//        given(postsService.readPosts(Mockito.anyLong())).willReturn(responseDto);
//
//        //when
//        ResultActions actions = mockMvc.perform(
//                get("/api/posts/{post-id}", postId)
//                        .with(csrf())
//                        .with(user(principalDetails))
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        //then
//        actions
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.postId").value(postId))
//                .andDo(document(
//                        "get-posts",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        pathParameters(
//                                parameterWithName("post-id").description("게시글 식별자")
//                        ),
//                        responseFields(
//                                List.of(
//                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
//                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
//                                        fieldWithPath("body").type(JsonFieldType.STRING).description("게시글 내용"),
//                                        fieldWithPath("leaderId").type(JsonFieldType.NUMBER).description("작성자/ 그룹장 식별자"),
//                                        fieldWithPath("leaderName").type(JsonFieldType.STRING).description("작성자/ 그룹장 이름"),
//                                        fieldWithPath("startDate").type(JsonFieldType.STRING).description("여행 시작 날짜(\"YYYY-MM-DD\")"),
//                                        fieldWithPath("endDate").type(JsonFieldType.STRING).description("여행 종료 날짜(\"YYYY-MM-DD\")"),
//                                        fieldWithPath("location").type(JsonFieldType.STRING).description("여행 지역"),
//                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("모집 인원"),
//                                        fieldWithPath("participants").type(JsonFieldType.ARRAY).description("참여 인원 목록"),
//                                        fieldWithPath("participants[].memberId").type(JsonFieldType.NUMBER).description("참여 인원 식별자"),
//                                        fieldWithPath("participants[].displayName").type(JsonFieldType.STRING).description("참여 인원 이름"),
//                                        fieldWithPath("participants[].profileImage").type(JsonFieldType.STRING).description("참여 인원 프로필 이미지 경로"),
//                                        fieldWithPath("participants[].content").type(JsonFieldType.STRING).description("참여 인원 자기소개"),
//                                        fieldWithPath("participantsCount").type(JsonFieldType.NUMBER).description("참여 인원 수"),
//                                        fieldWithPath("closeDate").type(JsonFieldType.STRING).description("모집 종료 날짜(\"YYYY-MM-DD\")"),
//                                        fieldWithPath("postsStatus").type(JsonFieldType.STRING).description("게시글 상태 " +
//                                                "(INACTIVE: 비활성화 / READY: 모집 예정 / RECRUITING: 모집 중 / COMPLETED: 모집 완료)"),
//                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성한 날짜"),
//                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("마지막으로 수정한 날짜")
//                                )
//                        )
//                ));
//    }
//
//    @Test
//    public void getAllPostsTest() throws Exception {
//        //given
//        Page<Posts> postsPage = new PageImpl<>(
//                List.of(
//                        Posts.builder().postId(1L).build(),
//                        Posts.builder().postId(2L).build()
//                ),
//                PageRequest.of(0, 10, Sort.by("postId").descending()), 2);
//
//
//        PostsDto.Response responseDto1 = PostsDto.Response.builder()
//                .postId(1L)
//                .title("사이판 여행 모집")
//                .body("사이판으로 여행가실 여자분 2명 구합니다. 여자 혼자 가기 너무 심심하네요. 호호호.")
//                .member(Member.builder().memberId(1L).displayName("테스트1").build())
//                .postsStatus(Posts.PostsStatus.READY)
//                .createdAt(LocalDateTime.of(2022, 9, 17, 12, 0, 0))
//                .modifiedAt(LocalDateTime.now())
//                .travelPeriod(new Period(LocalDate.of(2022, 11, 11),
//                        LocalDate.of(2022, 11, 14)))
//                .location("사이판")
//                .totalCount(3)
//                .closeDate(LocalDate.of(2022, 10, 12))
//                .participants(List.of(MemberDto.Participants.builder()
//                        .memberId(1L)
//                        .displayName("테스트1")
//                        .content("자기소개")
//                        .profileImage("")
//                        .build()))
//                .build();
//
//        PostsDto.Response responseDto2 = PostsDto.Response.builder()
//                .postId(2L)
//                .title("보라카이 혼자 가시는 분 같이 가요")
//                .body("보라카이로 여행가시는 남자분 1명 구합니다. 남자 혼자 가기 외롭네요. 허허허.")
//                .member(Member.builder().memberId(2L).displayName("테스트2").build())
//                .postsStatus(Posts.PostsStatus.READY)
//                .createdAt(LocalDateTime.of(2022, 9, 17, 12, 0, 0))
//                .modifiedAt(LocalDateTime.now())
//                .travelPeriod(new Period(LocalDate.of(2022, 11, 11),
//                        LocalDate.of(2022, 11, 14)))
//                .location("보라카이")
//                .totalCount(2)
//                .closeDate(LocalDate.of(2022, 10, 12))
//                .participants(List.of(MemberDto.Participants.builder()
//                        .memberId(2L)
//                        .displayName("테스트2")
//                        .content("자기소개")
//                        .profileImage("")
//                        .build()))
//                .build();
//
//        MultiResponseDto responseDto = new MultiResponseDto<>(
//                List.of(responseDto2, responseDto1), postsPage);
//
//        //mock
//        given(postsService.readAllPosts(Mockito.any(), Mockito.any())).willReturn(responseDto);
//
//        //when
//        ResultActions actions = mockMvc.perform(
//                get("/api/posts")
//                        .param("page", "1")
//                        .param("size", "10")
//                        .with(csrf())
//                        .with(user(principalDetails))
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//
//        //then
//        actions
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").isArray())
//                .andDo(document(
//                        "get-all-posts",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestParameters(
//                                List.of(
//                                        parameterWithName("page").description("Page 번호 (기본값 : 1)"),
//                                        parameterWithName("size").description("Page 크기 (기본값 : 10)"),
//                                        parameterWithName("_csrf").description("csrf").ignored(),
//                                        parameterWithName("title").description("제목 검색 키워드").optional(),
//                                        parameterWithName("body").description("내용 검색 키워드").optional(),
//                                        parameterWithName("location").description("지역 검색 키워드").optional(),
//                                        parameterWithName("startDate").description("여행 시작 날짜 검색 키워드 (YYYY-MM-DD)").optional(),
//                                        parameterWithName("endDate").description("여행 종료 날짜 검색 키워드 (YYYY-MM-DD)").optional(),
//                                        parameterWithName("sort").description("정렬 키워드" +
//                                                "(postId: 최신순/ startDate: 여행 시작 날짜순/ endDate: 여행 종료 날짜순/ " +
//                                                "closeDate: 모집 종료 날짜순/ totalCount: 모집 인원순/ limited: 남은 인원순").optional(),
//                                        parameterWithName("filters").description("필터 키워드(Recruiting: 모집중)").optional()
//                                )
//                        ),
//                        responseFields(
//                                List.of(
//                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("게시글 식별자"),
//                                        fieldWithPath("data[].postId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
//                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("게시글 제목"),
//                                        fieldWithPath("data[].body").type(JsonFieldType.STRING).description("게시글 내용"),
//                                        fieldWithPath("data[].leaderId").type(JsonFieldType.NUMBER).description("작성자/ 그룹장 식별자"),
//                                        fieldWithPath("data[].leaderName").type(JsonFieldType.STRING).description("작성자/ 그룹장 이름"),
//                                        fieldWithPath("data[].startDate").type(JsonFieldType.STRING).description("여행 시작 날짜(\"YYYY-MM-DD\")"),
//                                        fieldWithPath("data[].endDate").type(JsonFieldType.STRING).description("여행 종료 날짜(\"YYYY-MM-DD\")"),
//                                        fieldWithPath("data[].location").type(JsonFieldType.STRING).description("여행 지역"),
//                                        fieldWithPath("data[].totalCount").type(JsonFieldType.NUMBER).description("모집 인원"),
//                                        fieldWithPath("data[].participants").type(JsonFieldType.ARRAY).description("참여 인원 목록"),
//                                        fieldWithPath("data[].participants[].memberId").type(JsonFieldType.NUMBER).description("참여 인원 식별자"),
//                                        fieldWithPath("data[].participants[].displayName").type(JsonFieldType.STRING).description("참여 인원 이름"),
//                                        fieldWithPath("data[].participants[].profileImage").type(JsonFieldType.STRING).description("참여 인원 프로필 이미지 경로"),
//                                        fieldWithPath("data[].participants[].content").type(JsonFieldType.STRING).description("참여 인원 자기소개"),
//                                        fieldWithPath("data[].participantsCount").type(JsonFieldType.NUMBER).description("참여 인원 수"),
//                                        fieldWithPath("data[].closeDate").type(JsonFieldType.STRING).description("모집 종료 날짜(\"YYYY-MM-DD\")"),
//                                        fieldWithPath("data[].postsStatus").type(JsonFieldType.STRING).description("게시글 상태 " +
//                                                "(INACTIVE: 비활성화 / READY: 모집 예정 / RECRUITING: 모집 중 / COMPLETED: 모집 완료)"),
//                                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("작성한 날짜"),
//                                        fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING).description("마지막으로 수정한 날짜"),
//                                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호"),
//                                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
//                                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("전체 건 수"),
//                                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수")
//                                )
//                        )
//                ));
//    }
//
//    @Test
//    public void deletePosts() throws Exception {
//        //given
//        long postId = 1L;
//
//        doNothing().when(postsService).deletePosts(Mockito.anyLong(), Mockito.any(Authentication.class));
//
//        //when
//        ResultActions actions = mockMvc.perform(
//                delete("/api/posts/{post-id}", postId)
//                        .with(csrf())
//                        .with(user(principalDetails))
//        );
//
//        //then
//        actions
//                .andExpect(status().isNoContent())
//                .andDo(document("delete-posts",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        pathParameters(
//                                parameterWithName("post-id").description("게시글 식별자")
//                        )
//                ));
//    }
}
