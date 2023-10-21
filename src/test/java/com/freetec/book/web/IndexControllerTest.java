package com.freetec.book.web;

import com.freetec.book.service.posts.PostsService;
import com.freetec.book.web.dto.PostsSaveRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class IndexControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PostsService postsService;
    @Test
    @DisplayName("load_main_page")
    void loadMainPage() throws Exception {
        //when
        String body = this.restTemplate.getForObject("/", String.class);
        //then
        assertThat(body).contains("스프링 부트로 시작하는 웹 서비스");
    }
    @Test
    @DisplayName("load_save_page")
    void loadSavePage() throws Exception {
        //when
        String body = this.restTemplate.getForObject("/posts/save", String.class);
        //then
        assertThat(body).contains("게시글 등록");
        assertThat(body).contains("제목");
        assertThat(body).contains("작성자");
        assertThat(body).contains("내용");
    }
    @Test
    @DisplayName("load_modify_page")
    void loadModifyPage() throws Exception {
        //given
        Long savedId = postsService.save(PostsSaveRequestDto.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());
        //when
        String body = this.restTemplate.getForObject("/posts/update/" +savedId, String.class);
        //then
        assertThat(body).contains("게시글 수정");
        assertThat(body).contains("title");
        assertThat(body).contains("content");
        assertThat(body).contains("author");
    }
}