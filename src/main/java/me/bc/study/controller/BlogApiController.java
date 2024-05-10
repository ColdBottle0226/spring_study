package me.bc.study.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.bc.study.domain.Article;
import me.bc.study.dto.ArticleResponse;
import me.bc.study.dto.UpdateArticleRequest;
import me.bc.study.dto.AddArticleRequest;
import me.bc.study.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "블로그 api", description = "블로그 API")
public class BlogApiController {

  private final BlogService blogService;

  @PostMapping("/api/articles")
  @Operation(description = "게시물 저장")
  public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
    Article savedArticle = blogService.save(request);

    // 요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(savedArticle);
  }

  @GetMapping("/api/articles")
  public ResponseEntity<List<ArticleResponse>> findAllArticles() {
    List<ArticleResponse> articles = blogService.findAll()   // findAll : List<Article> 형태로 반환 -> ArticleResponse 형태로 변환
        .stream()
        .map(ArticleResponse::new)                          // Loop에서 ArticleResponse 생성자 호출 적용 (ArticleResponse response = new ArticleResponse();)
        .toList();

//    stream 동일 로직
//    List<Article> articleVO = blogService.findAll();
//    List<ArticleResponse> articleResponsesVO = new ArrayList<>();
//
//    for (int i = 0; i < articleVO.size(); i++) {
//      ArticleResponse response = new ArticleResponse();
//      response.setTitle(articlesVO.get(i).getTitle());
//      articleResponsesVO.add(response);
//    }


    return ResponseEntity.ok()
        .body(articles);
  }

  @GetMapping("/api/articles/{id}")
  public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
    Article article = blogService.findById(id);

    return ResponseEntity.ok()
        .body(new ArticleResponse(article));
  }

  @DeleteMapping("/api/articles/{id}")
  public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
    blogService.delete(id);

    return ResponseEntity.ok()
        .build();
  }

  @PutMapping("/api/articles/{id}")
  public ResponseEntity<Article> updateArticle(
      @PathVariable long id,
      @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = blogService.update(id, request);

    return ResponseEntity.ok()
        .body(updatedArticle);
  }
}
