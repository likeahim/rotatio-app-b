package com.app.rotatio.domain.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResponseDtoTest {

    @Test
    public void testGetters() {
        String url = "http://example.com/pdf";
        String name = "Test PDF";

        ResponseDto responseDto = new ResponseDto();
        responseDto.setUrl(url);
        responseDto.setName(name);

        assertEquals(url, responseDto.getUrl());
        assertEquals(name, responseDto.getName());
    }

    @Test
    public void testGettersWithIncorrectValues() {
        String url = "http://example.com/pdf";
        String name = "Test PDF";

        ResponseDto responseDto = new ResponseDto();
        responseDto.setUrl("http://wrong-url.com");
        responseDto.setName("Wrong Name");

        assertNotEquals(url, responseDto.getUrl());
        assertNotEquals(name, responseDto.getName());
    }
}