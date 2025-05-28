package com.example;

import org.junit.jupiter.api.Test;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DateInfoServletTest {

    // Subclass to expose protected doGet
    static class TestableDateInfoServlet extends DateInfoServlet {
        @Override
        public void doGet(HttpServletRequest req, HttpServletResponse res) {
            try {
                super.doGet(req, res);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private HttpServletResponse mockResponseWithWriter(StringWriter output) throws Exception {
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = new PrintWriter(output);
        when(response.getWriter()).thenReturn(writer);
        return response;
    }

    private void testDate(String dob, String expectedSign, String expectedDay) throws Exception {
        DateInfoServlet servlet = new DateInfoServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("dob")).thenReturn(dob);

        StringWriter output = new StringWriter();
        HttpServletResponse response = mockResponseWithWriter(output);

        servlet.doPost(request, response);

        output.flush();
        String html = output.toString();
        System.out.println("Rendered HTML:\n" + html); // DEBUG

        assertTrue(html.toLowerCase().contains(expectedSign.toLowerCase()), "Expected zodiac sign: " + expectedSign);
        assertTrue(html.contains(expectedDay), "Expected day of week: " + expectedDay);
    }


    @Test
    void testValidZodiacPisces() throws Exception {
        // March 10, 2000 = Pisces, Friday
        testDate("2000-03-10", "Pisces", "Friday");
    }

    @Test
    void testValidZodiacLeo() throws Exception {
        // July 30, 1990 = Leo, Monday
        testDate("1990-07-30", "Leo", "Monday");
    }

    @Test
    void testValidZodiacSagittarius() throws Exception {
        // December 20, 1985 = Sagittarius, Friday
        testDate("1985-12-20", "Sagittarius", "Friday");
    }

    // @Test
    // void testValidZodiacCapricorn() throws Exception {
    //     // December 20, 1985 = Capricorn, Friday
    //     testDate("1985-12-20", "Capricorn", "Friday");
    // }

    @Test
    void testInvalidDateTriggersError() throws Exception {
        DateInfoServlet servlet = new DateInfoServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("dob")).thenReturn("not-a-date");

        servlet.doPost(request, response);
        verify(response).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), contains("Invalid input"));
    }

    @Test
    void testDoGetOutput() throws Exception {
        TestableDateInfoServlet servlet = new TestableDateInfoServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        StringWriter output = new StringWriter();
        HttpServletResponse response = mockResponseWithWriter(output);

        servlet.doGet(request, response);

        output.flush();
        String html = output.toString();
        assertTrue(html.contains("only supports POST requests"));
        assertTrue(html.contains("Back"));
    }

    @Test
    void testHeadersAreSet() throws Exception {
        DateInfoServlet servlet = new DateInfoServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("dob")).thenReturn("2001-05-15");

        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        servlet.doPost(request, response);

        verify(response).setHeader("X-Frame-Options", "DENY");
        verify(response).setHeader("X-Content-Type-Options", "nosniff");
        verify(response).setHeader("Content-Security-Policy", "default-src 'self'");
        verify(response).setHeader("Permissions-Policy", "geolocation=(), microphone=()");
        verify(response).setHeader("Cross-Origin-Opener-Policy", "same-origin");
        verify(response).setHeader("Cross-Origin-Embedder-Policy", "require-corp");
        verify(response).setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        verify(response).setHeader("Pragma", "no-cache");
    }
}
