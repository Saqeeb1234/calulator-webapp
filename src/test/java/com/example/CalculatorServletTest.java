package com.example;

import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

class CalculatorServlet extends HttpServlet {

    // Make doGet public, to match your test subclass visibility
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();  // call once and reuse

        out.println("<html><body>");
        out.println("<h1>Calculator Servlet</h1>");
        out.println("<p>Welcome to Calculator Servlet</p>");
        out.println("</body></html>");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();  // call once

        String num1Str = request.getParameter("num1");
        String num2Str = request.getParameter("num2");
        String operator = request.getParameter("operator");

        double num1, num2, result;

        try {
            num1 = Double.parseDouble(num1Str);
            num2 = Double.parseDouble(num2Str);

            switch (operator) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "*":
                    result = num1 * num2;
                    break;
                case "/":
                    if (num2 == 0) {
                        result = Double.NaN;
                    } else {
                        result = num1 / num2;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operator");
            }

            out.println("Result: " + result);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid input");
        }
    }
}
