package me.bc.study.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import me.bc.study.domain.Article;
import me.bc.study.dto.AddArticleRequest;
import me.bc.study.dto.UpdateArticleRequest;
import me.bc.study.repository.BlogRepository;
import me.bc.study.service.BlogService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)     // Mockito 클래스 사용 명시
class BlogServiceControllerTest {

  // BlogRepository 주입 ( 빈 껍데기 객체 -> service 수행해도 return X)
  @Mock
  private BlogRepository blogRepository;

  @InjectMocks
  private BlogService blogService;

  // 글 생성
  @DisplayName("블로그 글을 정상적으로 저장한다.")
  @Test
  void testSave() {

    //given(초기 조건/환경/제약조건)
    String expectedTitle = "제목12";
    String expectedContent = "내용12";
    LocalDateTime now = LocalDateTime.now();
    AddArticleRequest request = new AddArticleRequest(expectedTitle, expectedContent);


    Article expectedArticle = new Article(1L, expectedTitle, expectedContent, now, now);
    // repository save에 역할 부여(mockito 의 given, blogRepository의 save에 어떤 값이 들어와도 Article 객체 반환)
    given(blogRepository.save(any())).willReturn(expectedArticle);


    //when(save 수행시)
    Article result = blogService.save(request);
    
    //then(결과 검증))
    assertThat(result).isNotNull();  // Mock 객체이기 때문에 return 없음
    assertThat(result.getTitle()).isEqualTo(expectedTitle);
    assertThat(result.getContent()).isEqualTo(expectedContent);

    //  같은 의미
    //  assertThat(result).isNotNull().isEqualToComparingFieldByField(expectedTitle);

  }

  @Test
  @DisplayName("모든 블로그 글을 가져온다.")
  void testFindAll() {

    // Given
    List<Article> articles = Arrays.asList(
        new Article(1L, "제목1", "내용1", LocalDateTime.now(), LocalDateTime.now()),
        new Article(2L, "제목2", "내용2", LocalDateTime.now(), LocalDateTime.now())
    );

    given(blogRepository.findAll()).willReturn(articles);

    // When (findAll 수행시 = Mock Service)
    List<Article> result = blogService.findAll();

    // Then
    assertThat(result).isNotNull();
    assertThat(result).hasSize(2);
    assertThat(result).containsExactlyElementsOf(articles);
  }

  @Test
  @DisplayName("주어진 ID로 블로그 글을 찾는다.")
  void testFindById() {
    // Given
    long id = 1L;
    Article article = new Article(id, "Title", "Content", LocalDateTime.now(), LocalDateTime.now());
    given(blogRepository.findById(id)).willReturn(Optional.of(article));

    // When
    Article result = blogService.findById(id);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(article);
  }

  @Test
  @DisplayName("주어진 ID로 블로그 글을 찾을 수 없으면 예외가 발생한다.")
  void testFindByIdNotFound() {
    // Given
    long id = 1L;
    given(blogRepository.findById(id)).willReturn(Optional.empty());

    // When / Then
    assertThatThrownBy(() -> blogService.findById(id))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("not found : " + id);
  }

  @Test
  @DisplayName("주어진 ID로 블로그 글을 삭제한다.")
  void testDelete() {
    // Given
    List<Article> articles = Arrays.asList(
        new Article(1L, "제목1", "내용1", LocalDateTime.now(), LocalDateTime.now()),
        new Article(2L, "제목2", "내용2", LocalDateTime.now(), LocalDateTime.now())
    );
    given(blogRepository.findAll()).willReturn(articles);

    // When
    // 삭제 수행
    blogService.delete(1L);
    List<Article> result = blogService.findAll();

    // Then
    assertThat(result.size()).isEqualTo(1);
  }

  @Test
  @DisplayName("주어진 ID로 블로그 글을 삭제한다.")
  void testDeleteCount() {
    // Given
    long id = 1L;

    // When
    blogService.delete(id);

    // Then
    // deleteById 메서드가 한 번 호출되었는지를 검증합니다.
    verify(blogRepository, times(1)).deleteById(id);
  }

  @Test
  @DisplayName("주어진 ID와 요청으로 블로그 글을 업데이트한다.")
  void testUpdate() {
    // Given
    long id = 1L;
    UpdateArticleRequest request = new UpdateArticleRequest("New Title", "New Content");
    Article article = new Article(id, "Title", "Content", LocalDateTime.now(), LocalDateTime.now());

    given(blogRepository.findById(id)).willReturn(Optional.of(article));

    // When
    Article result = blogService.update(id, request);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getTitle()).isEqualTo(request.getTitle());
    assertThat(result.getContent()).isEqualTo(request.getContent());
  }



}
