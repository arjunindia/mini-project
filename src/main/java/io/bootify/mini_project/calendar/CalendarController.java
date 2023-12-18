package io.bootify.mini_project.calendar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        List<List<Integer>> weeks = new ArrayList<>();

        // Populate the weeks list with day numbers
        List<Integer> week = new ArrayList<>();
        for (int i = 0; i < firstDayOfWeek.getValue(); i++) {
            week.add(null); // Add empty cells for the days before the first day of the month
        }
        for (int day = 1; day <= daysInMonth; day++) {
            week.add(day);
            if (week.size() == 7 || day == daysInMonth) {
                weeks.add(new ArrayList<>(week));
                week.clear();
            }
        }

        // Add the weeks list to the model
        model.addAttribute("weeks", weeks);

        // Return the Thymeleaf template name
        return "calendar/calendar";
    }
}
