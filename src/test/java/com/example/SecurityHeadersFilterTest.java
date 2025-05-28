package com.example;

import org.junit.jupiter.api.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

public class SecurityHeadersFilterTest {

    @Test
    void testAllSecurityHeadersAreSet() throws Exception {
        SecurityHeadersFilter filter = new SecurityHeadersFilter();

        // Mocks
        ServletRequest request = mock(ServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        // Execute
        filter.doFilter(request, response, chain);

        // Verify headers
        verify(response).setHeader("X-Frame-Options", "DENY");
        verify(response).setHeader("X-Content-Type-Options", "nosniff");
        verify(response).setHeader("Content-Security-Policy", "default-src 'self'");
        verify(response).setHeader("Permissions-Policy", "geolocation=(), microphone=()");
        verify(response).setHeader("Cross-Origin-Opener-Policy", "same-origin");
        verify(response).setHeader("Cross-Origin-Embedder-Policy", "require-corp");
        verify(response).setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        verify(response).setHeader("Pragma", "no-cache");

        // Ensure chain continues
        verify(chain).doFilter(request, response);
    }

    @Test
    void testNonHttpServletResponseSkipsHeaders() throws Exception {
        SecurityHeadersFilter filter = new SecurityHeadersFilter();

        ServletRequest request = mock(ServletRequest.class);
        ServletResponse response = mock(ServletResponse.class); // Not HttpServletResponse
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        // Verify chain proceeds
        verify(chain).doFilter(request, response);
        // Ensure no HttpServletResponse-specific methods are called
        verifyNoMoreInteractions(response);
    }
}
