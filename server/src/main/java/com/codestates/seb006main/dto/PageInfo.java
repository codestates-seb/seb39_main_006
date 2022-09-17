package com.codestates.seb006main.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageInfo {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public PageInfo(Page page) {
        this.page = page.getNumber() + 1;
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}
