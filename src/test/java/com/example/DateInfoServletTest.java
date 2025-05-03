import com.example.DateInfoServlet;
import org.junit.jupiter.api.Test;

import javax.servlet.http.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

public class DateInfoServletTest {

    @Test
    void testDoPostReturns200() throws Exception {
        DateInfoServlet servlet = new DateInfoServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("dob")).thenReturn("1990-01-01");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        servlet.doPost(request, response);

        verify(response, never()).sendError(anyInt(), anyString());
        pw.flush();
        assert sw.toString().contains("zodiac sign");
    }
}
