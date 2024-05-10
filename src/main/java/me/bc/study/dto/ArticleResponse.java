package me.bc.study.dto;

import lombok.Builder;
import lombok.Getter;
import me.bc.study.domain.Article;

@Getter
public class ArticleResponse {

    private final String title;
    private final String content;

    // Article 객체 처리
    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }

    // 방식 2
//    public ArticleResponse of(Article article) {
//        return ArticleResponse.builder()
//            .title(article.getTitle())
//            .content(article.getContent())
//            .build();
//    }
}
