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
                .body("????????? ?????????????????? ?????????")
                .images(new ArrayList<>())
                .build();
        String content = gson.toJson(postDto);

        FeedDto.Response responseDto = FeedDto.Response.builder()
                .feedId(1L)
                .body("????????? ?????????????????? ?????????")
                .member(Member.builder().memberId(1L).displayName("?????????").profileImage("").build())
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
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("images").type(JsonFieldType.ARRAY).description("????????? url ?????????")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("feedId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("????????? ????????? ??????"),
                                        fieldWithPath("comments").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("??????????????? ????????? ??????").optional()
                                )
                        )
                ));
    }

    @Test
    public void patchFeedTest() throws Exception {
        //given
        long feedId = 1L;
        FeedDto.Patch patchDto = FeedDto.Patch.builder()
                .body("????????? ?????????????????? ????????? ????????? ??? ?????? ?????????")
                .images(new ArrayList<>())
                .build();
        String content = gson.toJson(patchDto);

        FeedDto.Response responseDto = FeedDto.Response.builder()
                .feedId(1L)
                .body("????????? ?????????????????? ????????? ????????? ??? ?????? ?????????")
                .member(Member.builder().memberId(1L).displayName("?????????").profileImage("").build())
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
                                parameterWithName("feed-id").description("?????? ?????????")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("images").type(JsonFieldType.ARRAY).description("????????? url ?????????")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("feedId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("????????? ????????? ??????"),
                                        fieldWithPath("comments").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("??????????????? ????????? ??????").optional()
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
                .body("????????? ?????????????????? ????????? ????????? ??? ?????? ?????????")
                .member(Member.builder().memberId(1L).displayName("?????????").profileImage("").build())
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
                                parameterWithName("feed-id").description("?????? ?????????")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("feedId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("body").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                        fieldWithPath("memberName").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("????????? ????????? ??????"),
                                        fieldWithPath("comments").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("??????????????? ????????? ??????").optional()
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
                .body("????????? ?????????????????? ????????? ????????? ??? ?????? ?????????")
                .member(Member.builder().memberId(1L).displayName("?????????1").profileImage("").build())
                .createdAt(LocalDateTime.now().minusHours(1))
                .modifiedAt(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        FeedDto.Response responseDto2 = FeedDto.Response.builder()
                .feedId(2L)
                .body("????????? ????????? ?????????????????? ?????? ???????????????")
                .member(Member.builder().memberId(2L).displayName("?????????2").profileImage("").build())
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
                                        parameterWithName("page").description("Page ?????? (????????? : 1)"),
                                        parameterWithName("size").description("Page ?????? (????????? : 10)"),
                                        parameterWithName("_csrf").description("csrf").ignored()
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("????????? ?????????"),
                                        fieldWithPath("data[].feedId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("data[].body").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("data[].memberId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                        fieldWithPath("data[].memberName").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("data[].profileImage").type(JsonFieldType.STRING).description("????????? ????????? ??????"),
                                        fieldWithPath("data[].comments").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING).description("??????????????? ????????? ??????").optional(),
                                        fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("????????? ??????"),
                                        fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("????????? ??????"),
                                        fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("?????? ??? ???"),
                                        fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("?????? ????????? ???")
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
                                parameterWithName("feed-id").description("?????? ?????????")
                        )
                ));
    }
}
