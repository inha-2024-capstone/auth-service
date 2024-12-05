package com.mog.authserver.security.thirdparty.util;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class CookieUtilsTest {

    @Test
    @DisplayName("쿠키 추가 테스트")
    void 쿠키_추가_테스트(){
        //when
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        CookieUtils.addCookie(mockHttpServletResponse,
                "temp",
                "temp",
                180);
        boolean isThereCookie = false;
        //when
        for(Cookie cookie : mockHttpServletResponse.getCookies()){
            if(cookie.getName().equals("temp"))
                isThereCookie = true;
        }
        //then
        Assertions.assertTrue(isThereCookie);
    }

    @Test
    @DisplayName("쿠키 추가 테스트")
    void 쿠키_삭제_테스트(){
        //given
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        Cookie newCookie = new Cookie("temp", "temp");
        newCookie.setPath("/");
        newCookie.setHttpOnly(true);
        newCookie.setMaxAge(180);
        mockHttpServletRequest.setCookies(newCookie);

        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        //when
        CookieUtils.deleteCookie(mockHttpServletRequest, mockHttpServletResponse, "temp");
        boolean isExpired = false;
        for(Cookie cookie : mockHttpServletResponse.getCookies()){
            if(cookie.getName().equals("temp")){
                isExpired = (cookie.getMaxAge() == 0);
            }
        }
        //then
        Assertions.assertTrue(isExpired);
    }

    @Test
    @DisplayName("직렬화 테스트")
    void 직렬화_테스트(){
        //given
        String nonSerialized = "HI";
        //when
        String serialize = CookieUtils.serialize(nonSerialized);
        //then
        Assertions.assertEquals("rO0ABXQAAkhJ", serialize);
    }

    @Test
    @DisplayName("역직렬화 테스트")
    void 역직렬화_테스트(){
        //given
        String nonSerialized = "HI";
        String serialize = CookieUtils.serialize(nonSerialized);
        Cookie cookie = new Cookie("temp", serialize);

        //when
        String deserialize = CookieUtils.deserialize(cookie, String.class);

        //then
        Assertions.assertEquals(nonSerialized, deserialize);
    }

}