package com.freetec.book.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
class ProfileControllerTest {
    @Test
    @DisplayName("realProfie이 조회된다")
    void realProfie이_조회된다() throws Exception {
        //given
        String expectedProfile = "real";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("oauth");
        env.addActiveProfile("real-db");

        ProfileController controller = new ProfileController(env);
        //when
        String profile = controller.profile();

        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    @DisplayName("real_프로필이 없으면 첫번째가 조회된다")
    void real_프로필이_없으면_첫번째가_조회된다() throws Exception {
        //given
        String expectedProfile = "oauth";
        MockEnvironment env = new MockEnvironment();

        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("real-db");

        ProfileController controller = new ProfileController(env);

        //when
        String profile = controller.profile();
        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    @DisplayName("active 프로필이 없으면 default가 조회된다")
    void active_프로필이_없으면_default가_조회된다() throws Exception {
        //given
        String expectedProfile = "default";
        MockEnvironment env = new MockEnvironment();
        ProfileController controller = new ProfileController(env);

        //when
        String profile = controller.profile();
        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }
}