package com.codestates.seb006main.Image.mapper;

import com.codestates.seb006main.Image.dto.ImageDto;
import com.codestates.seb006main.Image.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageDto.Response imageToResponseDto(Image image);
    @Named("imageListToResponseDtoList")
    List<ImageDto.Response> imageListToResponseDtoList(List<Image> images);
}
