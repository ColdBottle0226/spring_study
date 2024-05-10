package me.bc.study.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Schema(description = "게시물 업데이트 요청 DTO")
public class UpdateArticleRequest {
    private String title;
    private String content;
}
