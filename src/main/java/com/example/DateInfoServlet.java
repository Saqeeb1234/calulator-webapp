package com.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;

@WebServlet("/dateinfo")
public class DateInfoServlet extends HttpServlet {

    private String getZodiacSign(LocalDate date) {
        int day = date.getDayOfMonth();
        Month month = date.getMonth();
        String sign = "";

        switch (month) {
            case JANUARY:
                sign = (day <= 19) ? "Capricorn" : "Aquarius";
                break;
            case FEBRUARY:
                sign = (day <= 18) ? "Aquarius" : "Pisces";
                break;
            case MARCH:
                sign = (day <= 20) ? "Pisces" : "Aries";
                break;
            case APRIL:
                sign = (day <= 19) ? "Aries" : "Taurus";
                break;
            case MAY:
                sign = (day <= 20) ? "Taurus" : "Gemini";
                break;
            case JUNE:
                sign = (day <= 20) ? "Gemini" : "Cancer";
                break;
            case JULY:
                sign = (day <= 22) ? "Cancer" : "Leo";
                break;
            case AUGUST:
                sign = (day <= 22) ? "Leo" : "Virgo";
                break;
            case SEPTEMBER:
                sign = (day <= 22) ? "Virgo" : "Libra";
                break;
            case OCTOBER:
                sign = (day <= 22) ? "Libra" : "Scorpio";
                break;
            case NOVEMBER:
                sign = (day <= 21) ? "Scorpio" : "Sagittarius";
                break;
            case DECEMBER:
                sign = (day <= 21) ? "Sagittarius" : "Capricorn";
                break;
        }

        return sign;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dobInput = request.getParameter("dob");

        LocalDate dob = LocalDate.parse(dobInput);
        DayOfWeek dayOfWeek = dob.getDayOfWeek();
        String zodiac = getZodiacSign(dob);

        response.setContentType("text/html");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h2>You were born on a: " +
                dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "</h2>");
        response.getWriter().println("<h3>Your zodiac sign is: " + zodiac + "</h3>");
        // XSS vulnerable echo
        response.getWriter().println("<p>You entered: " + dobInput + "</p>");  // adding for snyk test
        response.getWriter().println("<br><a href='index.html'>Back to Dashboard</a>");
        response.getWriter().println("</body></html>");
    }
}
