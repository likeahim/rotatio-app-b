package com.app.rotatio.domain.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class RequestDtoTest {

    @Test
    public void testGettersAndSetters() {
        RequestDto requestDto = new RequestDto();
        LocalDate date = LocalDate.now();
        String html = "<html></html>";
        String name = "Test Document";
        String margins = "1cm";
        String paperSize = "A4";
        String orientation = "portrait";
        boolean printBackground = true;
        boolean async = false;
        String footer = "Footer content";
        String header = "Header content";

        requestDto.setDate(date);
        requestDto.setHtml(html);
        requestDto.setName(name);
        requestDto.setMargins(margins);
        requestDto.setPaperSize(paperSize);
        requestDto.setOrientation(orientation);
        requestDto.setPrintBackground(printBackground);
        requestDto.setAsync(async);
        requestDto.setFooter(footer);
        requestDto.setHeader(header);

        assertEquals(date, requestDto.getDate());
        assertNotEquals(date.plusDays(1), requestDto.getDate());
        assertEquals(html, requestDto.getHtml());
        assertNotEquals(null, requestDto.getHtml());
        assertEquals(name, requestDto.getName());
        assertNotEquals(null, requestDto.getName());
        assertEquals(margins, requestDto.getMargins());
        assertNotEquals(null, requestDto.getMargins());
        assertEquals(paperSize, requestDto.getPaperSize());
        assertNotEquals(null, requestDto.getPaperSize());
        assertEquals(orientation, requestDto.getOrientation());
        assertNotEquals(null, requestDto.getOrientation());
        assertTrue(requestDto.isPrintBackground());
        assertNotEquals(false, requestDto.isPrintBackground());
        assertEquals(async, requestDto.isAsync());
        assertNotEquals(true, requestDto.isAsync());
        assertEquals(footer, requestDto.getFooter());
        assertEquals(header, requestDto.getHeader());
    }
}