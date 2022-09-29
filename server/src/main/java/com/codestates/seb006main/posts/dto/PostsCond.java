package com.codestates.seb006main.posts.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostsCond {
    private String title;
    private String body;
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private String startDate;
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private String endDate;
    private String location;
    private List<String> filters = new ArrayList<>();
}
