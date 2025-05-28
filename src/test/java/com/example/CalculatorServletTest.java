package com.example;

import org.junit.jupiter.api.Test;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorServletTest {

    // Expose protected doGet() for testing
    static class TestableCalculatorServlet extends CalculatorServlet {
        @Override
        public void doGet(HttpServletRequest request, HttpServletResponse response) {
            try {
                super.doGet(request, response);
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

    private void testPost(String num1, String num2, String operator, String expectedOutput) throws Exception {
        CalculatorServlet servlet = new CalculatorServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("num1")).thenReturn(num1);
        when(request.getParameter("num2")).thenReturn(num2);
        when(request.getParameter("operator")).thenReturn(operator);

        StringWriter output = new StringWriter();
        HttpServletResponse response = mockResponseWithWriter(output);

        servlet.doPost(request, response);

        output.flush();
        assertTrue(output.toString().contains(expectedOutput), "Expected output to contain: " + expectedOutput);
    }

    @Test
    void testAddition() throws Exception {
        testPost("5", "3", "+", "Result: 8.0");
    }

    @Test
    void testSubtraction() throws Exception {
        testPost("10", "4", "-", "Result: 6.0");
    }

    @Test
    void testMultiplication() throws Exception {
        testPost("6", "7", "*", "Result: 42.0");
    }

    @Test
    void testDivision() throws Exception {
        testPost("20", "5", "/", "Result: 4.0");
    }

    @Test
    void testDivisionByZero() throws Exception {
        testPost("5", "0", "/", "Result: NaN");
    }

    @Test
    void testInvalidInputTriggersException() throws Exception {
        CalculatorServlet servlet = new CalculatorServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("num1")).thenReturn("invalid");
        when(request.getParameter("num2")).thenReturn("2");
        when(request.getParameter("operator")).thenReturn("+");

        servlet.doPost(request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), anyString());
    }

    @Test
    void testDoGet() throws Exception {
        TestableCalculatorServlet servlet = new TestableCalculatorServlet();

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
        CalculatorServlet servlet = new CalculatorServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("num1")).thenReturn("1");
        when(request.getParameter("num2")).thenReturn("2");
        when(request.getParameter("operator")).thenReturn("+");

        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        servlet.doPost(request, response);

        // Verify critical headers set
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
