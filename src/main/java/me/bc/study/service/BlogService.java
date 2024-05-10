package me.bc.study.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.bc.study.domain.Article;
import me.bc.study.dto.UpdateArticleRequest;
import me.bc.study.dto.AddArticleRequest;
import me.bc.study.repository.BlogRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlogService {

  private final BlogRepository blogRepository;

  public Article save(AddArticleRequest request) {
    return blogRepository.save(request.toEntity());
  }

  public List<Article> findAll() {
    return blogRepository.findAll();
  }

  public Article findById(long id) {
    return blogRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("not found : " + id));   // JPA 에서 조회시, null 반환 X, NUll처리 필요
  }
  public void delete(long id) {
    blogRepository.deleteById(id);
  }

  @Transactional
  public Article update(long id, UpdateArticleRequest request) {
    Article article = blogRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

    article.update(request.getTitle(), request.getContent());

    return article;
  }
}
