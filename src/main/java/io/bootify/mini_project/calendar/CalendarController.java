package io.bootify.mini_project.calendar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class Date {
    private int year;
    private int month;
    private int day;

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() { return day; }

    public int getDayOfWeek() { 
        LocalDate date = LocalDate.of(year, month, day);
        return date.getDayOfWeek().getValue();
    }

    public boolean isToday() { 
        if(year == 0 || month == 0 || day == 0) {
            return false;
        }
        LocalDate date = LocalDate.of(year, month, day);
        return date.equals(LocalDate.now());
     }

    public boolean isCurrentMonth() { 
        if(year == 0 || month == 0 || day == 0) {
            return false;
        }
        LocalDate date = LocalDate.of(year, month, day);
        return date.getMonthValue() == LocalDate.now().getMonthValue();
    }

    public int getTime() {
        return year * 10000 + month * 100 + day;
    }
}

@Controller
public class CalendarController {

    @GetMapping("/calendar")
    public String showCalendar(Model model) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Calculate the first day of the month
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);

        // Calculate the last day of the month
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

        // Calculate the number of days to display
        int daysInMonth = lastDayOfMonth.getDayOfMonth();

        // Calculate the day of the week for the first day of the month
        DayOfWeek firstDayOfWeek = firstDayOfMonth.getDayOfWeek();

        // Create a list to store the weeks
        List<List<Date>> weeks = new ArrayList<>();

        // Populate the weeks list with day numbers
        List<Date> week = new ArrayList<>();

        for (int i = 0; i < firstDayOfWeek.getValue(); i++) {
            week.add(new Date(0, 0, 0));
        }
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), day);
            week.add(new Date(date.getYear(), date.getMonthValue(), date.getDayOfMonth()));
            if (week.size() == 7 || day == daysInMonth) {
                weeks.add(week);
                week = new ArrayList<>();
            }
            // add next month days
            if (day == daysInMonth) {
                for (int i = 1; i <= 7 - lastDayOfMonth.getDayOfWeek().getValue(); i++) {
                    week.add(new Date(0, 0, 0));
                }
            }
        }

        // Add the weeks list to the model
        model.addAttribute("weeks", weeks);

        // Return the Thymeleaf template name
        return "calendar/calendar";
    }
}
