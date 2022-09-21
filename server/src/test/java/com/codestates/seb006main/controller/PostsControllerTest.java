package com.codestates.seb006main.controller;

import com.codestates.seb006main.dto.MultiResponseDto;
import com.codestates.seb006main.group.dto.GroupDto;
import com.codestates.seb006main.group.entity.Group;
import com.codestates.seb006main.posts.controller.PostsController;
import com.codestates.seb006main.posts.dto.PostsDto;
import com.codestates.seb006main.posts.entity.Posts;
import com.codestates.seb006main.posts.service.PostsService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostsController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class PostsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PostsService postsService;
    @Autowired
    private Gson gson;

    @Test
    public void postPostsTest() throws Exception {
        //given
        List<MultipartFile> images = new ArrayList<>();
        PostsDto.Post postDto = PostsDto.Post.builder()
                .title("사이판 여행 모집")
                .body("사이판으로 여행가실 여자분 2명 구합니다. 여자 혼자 가기 너무 심심하네요. 호호호.")
                .group(GroupDto.Post.builder()
//                        .startDate(LocalDate.of(2022, 11, 11))
//                        .endDate(LocalDate.of(2022, 11, 14))
                        .startDate("2022-11-11")
                        .endDate("2022-11-14")
                        .location("사이판")
                        .headcount(2)
//                        .closeDate(LocalDate.of(2022, 10, 12))
                        .closeDate("2022-10-12")
                        .build())
                .build();
        String content = gson.toJson(postDto);

        PostsDto.Response responseDto = PostsDto.Response.builder()
                .postId(1L)
                .title("사이판 여행 모집")
                .body("사이판으로 여행가실 여자분 2명 구합니다. 여자 혼자 가기 너무 심심하네요. 호호호.")
                .postsStatus(Posts.PostsStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .modifiedAt(null)
                .group(GroupDto.Response.builder()
                        .groupId(1L)
                        .travelPeriod(new Period(LocalDate.of(2022, 11, 11),
                                LocalDate.of(2022, 11, 14)))
                        .location("사이판")
                        .headcount(2)
                        .groupStatus(Group.GroupStatus.RECRUITING)
                        .closeDate(LocalDate.of(2022, 10, 12))
                        .build())
                .build();

        //mock
        given(postsService.createPosts(Mockito.any(PostsDto.Post.class))).willReturn(responseDto);

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/posts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(postDto.getTitle()))
                .andExpect(jsonPath("$.body").value(postDto.getBody()))
                .andDo(document(
                        "post-posts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("게시글 내용"),
                                        fieldWithPath("group").type(JsonFieldType.OBJECT).description("관련 모집 정보"),
                                        fieldWithPath("group.startDate").type(JsonFieldType.STRING).description("여행 시작 날짜"),
                                        fieldWithPath("group.endDate").type(JsonFieldType.STRING).description("여행 종료 날짜"),
                                        fieldWithPath("group.location").type(JsonFieldType.STRING).description("여행 지역"),
                                        fieldWithPath("group.headcount").type(JsonFieldType.NUMBER).description("모집 인원"),
                                        fieldWithPath("group.closeDate").type(JsonFieldType.STRING).description("모집 종료 날짜")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("게시글 내용"),
                                        fieldWithPath("postsStatus").type(JsonFieldType.STRING).description("게시글 상태"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성한 날짜"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("마지막으로 수정한 날짜").optional(),
                                        fieldWithPath("group").type(JsonFieldType.OBJECT).description("관련 모집 정보"),
                                        fieldWithPath("group.groupId").type(JsonFieldType.NUMBER).description("관련 모집 정보"),
                                        fieldWithPath("group.startDate").type(JsonFieldType.STRING).description("여행 시작 날짜"),
                                        fieldWithPath("group.endDate").type(JsonFieldType.STRING).description("여행 종료 날짜"),
                                        fieldWithPath("group.location").type(JsonFieldType.STRING).description("여행 지역"),
                                        fieldWithPath("group.headcount").type(JsonFieldType.NUMBER).description("모집 인원"),
                                        fieldWithPath("group.groupStatus").type(JsonFieldType.STRING).description("모집 상태"),
                                        fieldWithPath("group.closeDate").type(JsonFieldType.STRING).description("모집 종료 날짜")
                                )
                        )
                ));
    }

    @Test
    public void patchPostsTest() throws Exception {
        //given
        long postId = 1L;
        PostsDto.Patch patchDto = PostsDto.Patch.builder()
                .title("(급합니다) 사이판 여행 모집 구해봐요")
                .body("일정이 촉박해 빠르게 구해봅니다. 사이판으로 여행가실 여자분 2명 구합니다. 여자 혼자 가기 너무 심심하네요. 흑흑흑.")
//                .group(GroupDto.Post.builder()
//                        .startDate("2022-11-11")
//                        .endDate("2022-11-14")
//                        .location("사이판")
//                        .headcount(2)
//                        .closeDate("2022-10-12")
//                        .build())
                .build();
        String content = gson.toJson(patchDto);

        PostsDto.Response responseDto = PostsDto.Response.builder()
                .postId(1L)
                .title("(급합니다) 사이판 여행 모집 구해봐요")
                .body("일정이 촉박해 빠르게 구해봅니다. 사이판으로 여행가실 여자분 2명 구합니다. 여자 혼자 가기 너무 심심하네요. 흑흑흑.")
                .postsStatus(Posts.PostsStatus.ACTIVE)
                .createdAt(LocalDateTime.of(2022, 9, 17, 12, 0, 0))
                .modifiedAt(LocalDateTime.now())
                .group(GroupDto.Response.builder()
                        .groupId(1L)
                        .travelPeriod(new Period(LocalDate.of(2022, 11, 11),
                                LocalDate.of(2022, 11, 14)))
                        .location("사이판")
                        .headcount(2)
                        .groupStatus(Group.GroupStatus.RECRUITING)
                        .closeDate(LocalDate.of(2022, 10, 12))
                        .build())
                .build();

        //mock
        given(postsService.updatePosts(Mockito.anyLong(), Mockito.any(PostsDto.Patch.class))).willReturn(responseDto);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/api/posts/{post-id}", postId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(patchDto.getTitle()))
                .andExpect(jsonPath("$.body").value(patchDto.getBody()))
                .andDo(document(
                        "patch-posts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("게시글 내용")
//                                        fieldWithPath("group").type(JsonFieldType.OBJECT).description("관련 모집 정보"),
//                                        fieldWithPath("group.startDate").type(JsonFieldType.STRING).description("여행 시작 날짜"),
//                                        fieldWithPath("group.endDate").type(JsonFieldType.STRING).description("여행 종료 날짜"),
//                                        fieldWithPath("group.location").type(JsonFieldType.STRING).description("여행 지역"),
//                                        fieldWithPath("group.headcount").type(JsonFieldType.NUMBER).description("모집 인원"),
//                                        fieldWithPath("group.closeDate").type(JsonFieldType.STRING).description("모집 종료 날짜")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("게시글 내용"),
                                        fieldWithPath("postsStatus").type(JsonFieldType.STRING).description("게시글 상태"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성한 날짜"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("마지막으로 수정한 날짜"),
                                        fieldWithPath("group").type(JsonFieldType.OBJECT).description("관련 모집 정보"),
                                        fieldWithPath("group.groupId").type(JsonFieldType.NUMBER).description("관련 모집 정보"),
                                        fieldWithPath("group.startDate").type(JsonFieldType.STRING).description("여행 시작 날짜"),
                                        fieldWithPath("group.endDate").type(JsonFieldType.STRING).description("여행 종료 날짜"),
                                        fieldWithPath("group.location").type(JsonFieldType.STRING).description("여행 지역"),
                                        fieldWithPath("group.headcount").type(JsonFieldType.NUMBER).description("모집 인원"),
                                        fieldWithPath("group.groupStatus").type(JsonFieldType.STRING).description("모집 상태"),
                                        fieldWithPath("group.closeDate").type(JsonFieldType.STRING).description("모집 종료 날짜")
                                )
                        )
                ));
    }

    @Test
    public void getPostsTest() throws Exception {
        //given
        long postId = 1L;

        PostsDto.Response responseDto = PostsDto.Response.builder()
                .postId(1L)
                .title("(급합니다) 사이판 여행 모집 구해봐요")
                .body("일정이 촉박해 빠르게 구해봅니다. 사이판으로 여행가실 여자분 2명 구합니다. 여자 혼자 가기 너무 심심하네요. 흑흑흑.")
                .postsStatus(Posts.PostsStatus.ACTIVE)
                .createdAt(LocalDateTime.of(2022, 9, 17, 12, 0, 0))
                .modifiedAt(LocalDateTime.now())
                .group(GroupDto.Response.builder()
                        .groupId(1L)
                        .travelPeriod(new Period(LocalDate.of(2022, 11, 11),
                                LocalDate.of(2022, 11, 14)))
                        .location("사이판")
                        .headcount(2)
                        .groupStatus(Group.GroupStatus.RECRUITING)
                        .closeDate(LocalDate.of(2022, 10, 12))
                        .build())
                .build();

        //mock
        given(postsService.readPosts(Mockito.anyLong())).willReturn(responseDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/posts/{post-id}", postId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(postId))
                .andDo(document(
                        "get-posts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("게시글 내용"),
                                        fieldWithPath("postsStatus").type(JsonFieldType.STRING).description("게시글 상태"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("작성한 날짜"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("마지막으로 수정한 날짜"),
                                        fieldWithPath("group").type(JsonFieldType.OBJECT).description("관련 모집 정보"),
                                        fieldWithPath("group.groupId").type(JsonFieldType.NUMBER).description("관련 모집 정보"),
                                        fieldWithPath("group.startDate").type(JsonFieldType.STRING).description("여행 시작 날짜"),
                                        fieldWithPath("group.endDate").type(JsonFieldType.STRING).description("여행 종료 날짜"),
                                        fieldWithPath("group.location").type(JsonFieldType.STRING).description("여행 지역"),
                                        fieldWithPath("group.headcount").type(JsonFieldType.NUMBER).description("모집 인원"),
                                        fieldWithPath("group.groupStatus").type(JsonFieldType.STRING).description("모집 상태"),
                                        fieldWithPath("group.closeDate").type(JsonFieldType.STRING).description("모집 종료 날짜")
                                )
                        )
                ));
    }

    @Test
    public void getAllPostsTest() throws Exception {
        //given
        Page<Posts> postsPage = new PageImpl<>(
                List.of(
                        Posts.builder().postId(1L).build(),
                        Posts.builder().postId(2L).build()
                ),
                PageRequest.of(0, 10, Sort.by("postId").descending()), 2);


        PostsDto.Response responseDto1 = PostsDto.Response.builder()
                .postId(1L)
                .title("(급합니다) 사이판 여행 모집 구해봐요")
                .body("일정이 촉박해 빠르게 구해봅니다. 사이판으로 여행가실 여자분 2명 구합니다. 여자 혼자 가기 너무 심심하네요. 흑흑흑.")
                .postsStatus(Posts.PostsStatus.ACTIVE)
                .createdAt(LocalDateTime.of(2022, 9, 17, 12, 0, 0))
                .modifiedAt(LocalDateTime.now())
                .group(GroupDto.Response.builder()
                        .groupId(1L)
                        .travelPeriod(new Period(LocalDate.of(2022, 11, 11),
                                LocalDate.of(2022, 11, 14)))
                        .location("사이판")
                        .headcount(2)
                        .groupStatus(Group.GroupStatus.RECRUITING)
                        .closeDate(LocalDate.of(2022, 10, 12))
                        .build())
                .build();

        PostsDto.Response responseDto2 = PostsDto.Response.builder()
                .postId(2L)
                .title("보라카이 혼자 가시는 분 같이 가요")
                .body("보라카이로 여행가시는 남자분 1명 구합니다. 남자 혼자 가기 외롭네요. 허허허.")
                .postsStatus(Posts.PostsStatus.ACTIVE)
                .createdAt(LocalDateTime.of(2022, 9, 17, 12, 0, 0))
                .modifiedAt(LocalDateTime.now())
                .group(GroupDto.Response.builder()
                        .groupId(2L)
                        .travelPeriod(new Period(LocalDate.of(2022, 11, 11),
                                LocalDate.of(2022, 11, 14)))
                        .location("보라카이")
                        .headcount(2)
                        .groupStatus(Group.GroupStatus.RECRUITING)
                        .closeDate(LocalDate.of(2022, 10, 12))
                        .build())
                .build();

        MultiResponseDto responseDto = new MultiResponseDto<>(
                List.of(responseDto2, responseDto1), postsPage);

        //mock
        given(postsService.readAllPosts((Mockito.any()))).willReturn(responseDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/posts")
                        .param("page", "1")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document(
                        "get-all-posts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                List.of(
                                        parameterWithName("page").description("Page 번호 (기본값 : 1)"),
                                        parameterWithName("size").description("Page 크기 (기본값 : 10)")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("게시글 식별자"),
                                        fieldWithPath("data[].postId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("data[].body").type(JsonFieldType.STRING).description("게시글 내용"),
                                        fieldWithPath("data[].postsStatus").type(JsonFieldType.STRING).description("게시글 상태"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("작성한 날짜"),
                                        fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING).description("마지막으로 수정한 날짜"),
                                        fieldWithPath("data[].group").type(JsonFieldType.OBJECT).description("관련 모집 정보"),
                                        fieldWithPath("data[].group.groupId").type(JsonFieldType.NUMBER).description("관련 모집 정보"),
                                        fieldWithPath("data[].group.startDate").type(JsonFieldType.STRING).description("여행 시작 날짜"),
                                        fieldWithPath("data[].group.endDate").type(JsonFieldType.STRING).description("여행 종료 날짜"),
                                        fieldWithPath("data[].group.location").type(JsonFieldType.STRING).description("여행 지역"),
                                        fieldWithPath("data[].group.headcount").type(JsonFieldType.NUMBER).description("모집 인원"),
                                        fieldWithPath("data[].group.groupStatus").type(JsonFieldType.STRING).description("모집 상태"),
                                        fieldWithPath("data[].group.closeDate").type(JsonFieldType.STRING).description("모집 종료 날짜"),
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
        long postId = 1L;

        doNothing().when(postsService).deletePosts(Mockito.anyLong());

        //when
        ResultActions actions = mockMvc.perform(
                delete("/api/posts/{post-id}", postId)
        );

        //then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-posts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자")
                        )
                ));
    }
}
