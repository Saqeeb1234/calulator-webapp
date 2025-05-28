package com.example;

import com.example.DateInfoServlet;
import org.junit.jupiter.api.Test;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.mockito.Mockito.*;

public class DateInfoServletTest {

    // Inner subclass to expose protected doGet as public
    static class TestableDateInfoServlet extends DateInfoServlet {
        @Override
        public void doGet(HttpServletRequest request, HttpServletResponse response) {
            try {
                super.doGet(request, response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    void testValidDate() throws Exception {
        DateInfoServlet servlet = new DateInfoServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("dob")).thenReturn("2000-03-15");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        servlet.doPost(request, response);
        pw.flush();
        assert sw.toString().contains("zodiac sign");
    }

    @Test
    void testInvalidDate() throws Exception {
        DateInfoServlet servlet = new DateInfoServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("dob")).thenReturn("not-a-date");

        servlet.doPost(request, response);
        verify(response).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), anyString());
    }

    @Test
    void testGetMethod() throws Exception {
        TestableDateInfoServlet servlet = new TestableDateInfoServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = new PrintWriter(new StringWriter());

        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        // Accept 1 or more calls to getWriter()
        verify(response, atLeastOnce()).getWriter();
    }
}
