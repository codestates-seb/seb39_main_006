package com.codestates.seb006main.controller;

import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.posts.controller.MemberPostsController;
import com.codestates.seb006main.posts.service.PostsService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberPostsController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class MemberPostsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PostsService postsService;
    @MockBean
    private PrincipalDetails principalDetails;
    @Autowired
    private Gson gson;

    @Test
    public void deleteMemberPosts() throws Exception {
        //given
        long memberPostId = 1L;

        doNothing().when(postsService).deleteParticipant(Mockito.anyLong(), Mockito.any(Authentication.class));

        //when
        ResultActions actions = mockMvc.perform(
                delete("/api/participants/{memberPost-id}", memberPostId)
                        .with(csrf())
                        .with(user(principalDetails))
        );

        //then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-participants",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberPost-id").description("참여자 식별자")
                        )
                ));
    }
}
