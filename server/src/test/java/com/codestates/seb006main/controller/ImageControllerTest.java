package com.codestates.seb006main.controller;

import com.codestates.seb006main.Image.controller.ImageController;
import com.codestates.seb006main.Image.service.ImageService;
import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.posts.service.PostsService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class ImageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ImageService imageService;
    @MockBean
    private PrincipalDetails principalDetails;
    @Autowired
    private Gson gson;

    @Test
    public void uploadImageTest() throws Exception {
        //given
        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png", "<<png data>>".getBytes());

        Map<String, Object> response = new HashMap<>(){{
            put("imageId", 1L);
            put("imageUrl", "이미지 경로");
        }};

        given(imageService.uploadImage(Mockito.any(MultipartFile.class))).willReturn(response);

        //when
        ResultActions actions = mockMvc.perform(
                multipart("/api/images/upload")
                        .file(image)
                        .with(csrf())
                        .with(user(principalDetails))
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "upload-image",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("image").description("이미지 파일")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("imageId").type(JsonFieldType.NUMBER).description("업로드한 이미지 식별자"),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("업로드한 이미지 URL 경로")
                                )
                        )
                ));
    }

    @Test
    public void deleteImageTest() throws Exception {
        //given
        long imageId = 1L;

        doNothing().when(imageService).deleteImage(Mockito.anyLong());

        //when
        ResultActions actions = mockMvc.perform(
                delete("/api/images/{image-id}", imageId)
                        .with(csrf())
                        .with(user(principalDetails))
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document("delete-image",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("image-id").description("이미지 식별자")
                        )
                ));
    }

}
