package me.bc.study.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import me.bc.study.domain.Article;
import me.bc.study.dto.AddArticleRequest;
import me.bc.study.repository.BlogRepository;
import me.bc.study.service.BlogService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
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
    BDDMockito.given(blogRepository.save(any())).willReturn(expectedArticle);


    //when(save 수행시)
    Article result = blogService.save(request);
    
    //then(결과 검증))
    assertThat(result).isNotNull();  // Mock 객체이기 때문에 return 없음
    assertThat(result.getTitle()).isEqualTo(expectedTitle);
    assertThat(result.getContent()).isEqualTo(expectedContent);

    //  같은 의미
    //  assertThat(result).isNotNull().isEqualToComparingFieldByField(expectedTitle);

  }

}
