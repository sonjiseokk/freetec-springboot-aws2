package com.freetec.book.web;

import com.freetec.book.config.auth.SecurityConfig;
import com.freetec.book.web.dto.HelloResponseDto;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@WebMvcTest(controllers = HelloController.class,
excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityConfig.class)
})
class HelloControllerTest {
    @Autowired
    private MockMvc mvc;
    @Test
    @DisplayName("hello가_리턴된다")
    @WithMockUser(roles = "USER")
    void helloReturn() throws Exception {
        //given
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }


    @Test
    @DisplayName("롬복_기능테스트")
    @WithMockUser(roles = "USER")
    void lombokFuncTest() throws Exception {
        //given
        String name = "안녕";
        int amount = 1000;

        HelloResponseDto dto = new HelloResponseDto(name, amount);

        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
    }

    @Test
    @DisplayName("helloDto가_리턴된다")
    @WithMockUser(roles = "USER")
    void returnHelloDto() throws Exception {
        //given
        String name = "kim";
        int amount = 20000;

        mvc.perform(get("/hello/dto")
                        .param("name", name)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
        //when

        //then
    }

}