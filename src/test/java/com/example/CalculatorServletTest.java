import com.example.CalculatorServlet;
import org.junit.jupiter.api.Test;

import javax.servlet.http.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

public class CalculatorServletTest {

    @Test
    void testDoPostReturns200() throws Exception {
        CalculatorServlet servlet = new CalculatorServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("num1")).thenReturn("5");
        when(request.getParameter("num2")).thenReturn("3");
        when(request.getParameter("operator")).thenReturn("+");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        servlet.doPost(request, response);

        verify(response, never()).sendError(anyInt(), anyString());
        pw.flush();
        assert sw.toString().contains("Result: 8.0");
    }
}
