package com.freetec.book.web;

import com.freetec.book.config.auth.dto.SessionUser;
import com.freetec.book.service.posts.PostsService;
import com.freetec.book.web.dto.PostsSaveRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class IndexControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PostsService postsService;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mvc;

    private MockHttpSession createMockSession(SessionUser user) {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user); // 'user'는 세션에서 사용자 정보를 찾을 때 사용하는 키입니다.
        return session;
    }
    @BeforeEach
    public void setUp(){
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    @Test
    @DisplayName("메인페이지_로딩")
    @WithMockUser(roles = "USER")
    void loadMainPage() throws Exception {
        //when
        String body = this.restTemplate.getForObject("/", String.class);
        //then
//        assertThat(body).contains("스프링 부트로 시작하는 웹 서비스");

        mvc.perform(get("/"))
                .andExpect(content().string(containsString("스프링 부트로 시작하는 웹 서비스")));

    }
//    @Test
//    @DisplayName("저장페이지_로딩")
//    @WithMockUser(roles = "USER")
//    void loadSavePage() throws Exception {
//
//        //when
//        String body = this.restTemplate.getForObject("/posts/save", String.class);
//        //then
//        mvc.perform(get("/posts/save"))
//                .andExpect(content().string(containsString("게시글 등록")))
//                .andExpect(content().string(containsString("제목")))
//                .andExpect(content().string(containsString("내용")));
//    }
    @Test
    @DisplayName("수정페이지_로딩")
    @WithMockUser(roles = "USER")
    void loadModifyPage() throws Exception {
        //given
        Long savedId = postsService.save(PostsSaveRequestDto.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());
        //when
        String url = "/posts/update/" +savedId;
        //then
        mvc.perform(get(url))
                .andExpect(content().string(containsString("게시글 수정")))
                .andExpect(content().string(containsString("title")))
                .andExpect(content().string(containsString("content")))
                .andExpect(content().string(containsString("author")));
    }
}