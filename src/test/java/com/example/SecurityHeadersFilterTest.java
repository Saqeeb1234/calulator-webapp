import com.example.SecurityHeadersFilter;
import org.junit.jupiter.api.Test;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import static org.mockito.Mockito.*;

public class SecurityHeadersFilterTest {

    @Test
    void testHeadersSet() throws Exception {
        SecurityHeadersFilter filter = new SecurityHeadersFilter();
        ServletRequest request = mock(ServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(response).setHeader("X-Frame-Options", "DENY");
        verify(response).setHeader("X-Content-Type-Options", "nosniff");
        verify(response).setHeader("Content-Security-Policy", "default-src 'self'");
        verify(response).setHeader("Permissions-Policy", "geolocation=(), microphone=()");
        verify(response).setHeader("Cross-Origin-Opener-Policy", "same-origin");
        verify(response).setHeader("Cross-Origin-Embedder-Policy", "require-corp");
        verify(response).setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        verify(response).setHeader("Pragma", "no-cache");

        verify(chain).doFilter(request, response);
    }
}
