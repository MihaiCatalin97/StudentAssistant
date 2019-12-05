package com.lonn.scheduleparser;

import com.lonn.scheduleparser.parsing.services.OneTimeClassParsingService;
import com.lonn.scheduleparser.parsing.services.RecurringClassParsingService;
import com.lonn.scheduleparser.parsing.services.CourseParsingService;
import com.lonn.scheduleparser.parsing.services.OtherActivityParsingService;
import com.lonn.scheduleparser.parsing.services.ProfessorParsingService;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UAICScheduleParser {

    private static ProfessorParsingService professorParsingService;
    private static CourseParsingService courseParsingService;
    private static OtherActivityParsingService otherActivityParsingService;
    private static RecurringClassParsingService recurringClassParsingService;
    private static OneTimeClassParsingService oneTimeClassParsingService;

    static {
        professorParsingService = ProfessorParsingService.getInstance();
        courseParsingService = CourseParsingService.getInstance();
        otherActivityParsingService = OtherActivityParsingService.getInstance();
        recurringClassParsingService = RecurringClassParsingService.getInstance();
        oneTimeClassParsingService = OneTimeClassParsingService.getInstance();
    }

    public Future<ParseResult> parseUAICSchedule() {
        return Executors.newSingleThreadExecutor().submit(() -> {
            professorParsingService.deleteAll();
            courseParsingService.deleteAll();
            otherActivityParsingService.deleteAll();
            recurringClassParsingService.deleteAll();

            ParseResult parseResult = new ParseResult();

            parseResult.setProfessors(professorParsingService.getAll())
                    .setCourses(courseParsingService.getAll())
                    .setOtherActivities(otherActivityParsingService.getAll())
                    .setRecurringClasses(recurringClassParsingService.getAll())
                    .setOneTimeClasses(oneTimeClassParsingService.getAll());

            return parseResult;
        });
    }
}
