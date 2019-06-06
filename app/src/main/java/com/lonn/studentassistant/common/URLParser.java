package com.lonn.studentassistant.common;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Exam;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.entities.ScheduleClass;
import com.lonn.studentassistant.services.implementations.coursesService.dataAccessLayer.CourseRepository;
import com.lonn.studentassistant.services.implementations.examService.dataAccessLayer.ExamRepository;
import com.lonn.studentassistant.services.implementations.professorService.dataAccessLayer.ProfessorRepository;
import com.lonn.studentassistant.services.implementations.scheduleService.dataAccessLayer.ScheduleRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class URLParser
{
    private CourseRepository courseRepository;
    private ProfessorRepository professorRepository;
    private ScheduleRepository scheduleRepository;
    private ExamRepository examRepository;

    private String urlString;

    URLParser(String urlString)
    {
        this.urlString = urlString;
        courseRepository = CourseRepository.getInstance(null);
        professorRepository = ProfessorRepository.getInstance(null);
        scheduleRepository = ScheduleRepository.getInstance(null);
        examRepository = ExamRepository.getInstance(null);
    }

    void parse()
    {
        new ParserThread().start();
    }

    private List<Professor> parseProfessors(String htmlDocument)
    {
        List<Professor> professors = new ArrayList<>();
        final String REGEX = "(?i)<li>(<a.*?>(.+?)</a>)";
        Pattern p = Pattern.compile(REGEX);

        htmlDocument = htmlDocument.replace("\n","").replace("  ","");

        Matcher m = p.matcher(htmlDocument);

        while(m.find())
        {
            String professorLine = htmlDocument.substring(m.start(1), m.end(1));

            ValueLinkPair pair = new ValueLinkPair(professorLine);
            professors.add(pair.toProfessor());
        }

        return professors;
    }

    private String readUrl(String urlString)
    {
        try
        {
            URL url = new URL(urlString);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            url.openStream()));

            StringBuilder total = new StringBuilder();

            for (String line; (line = in.readLine()) != null; )
            {
                total.append(line).append('\n');
            }

            in.close();
            return total.toString();
        }
        catch (Exception e)
        {
            Log.e("Error", e.getMessage());
        }

        return "";
    }

    private List<ParsingRow> parseTable(String htmlDocument)
    {
        int day = 0;
        String examDate = "";
        List<ParsingRow> parsingRows = new ArrayList<>();
        String pattern = "(?i)<tr.*?>(.+?)</tr>";
        Pattern p = Pattern.compile(pattern);

        htmlDocument = htmlDocument.replace("\n","").replace("  ","");

        Matcher m = p.matcher(htmlDocument);

        while(m.find())
        {
            String row = htmlDocument.substring(m.start(1), m.end(1)).trim();

            if(!row.contains("th"))
            {
                if(row.contains("colspan"))
                {
                    row = removeAllTags(parseRow(row).get(0).value);
                    examDate = "";

                    if(row.contains(","))
                    {
                        day = Utils.dayToInt(row.split(",")[0]);
                        examDate = row.split(",")[1].trim();
                    }
                    else
                        day = Utils.dayToInt(row.split(" ")[0]);
                }
                else
                {
                    List<ValueLinkPair> pairs = parseRow(row);

                    parsingRows.add(new ParsingRow(pairs, day, examDate));
                }
            }
        }

        return parsingRows;
    }

    private List<ValueLinkPair> parseRow(String row)
    {
        List<ValueLinkPair> pairs = new ArrayList<>();
        String pattern = "(?i)<td.*?>(.+?)</td>";
        Pattern p = Pattern.compile(pattern);

        Matcher m = p.matcher(row);

        while(m.find())
        {
            String element = row.substring(m.start(1), m.end(1)).trim();
            pairs.add(new ValueLinkPair(element));
        }

        return pairs;
    }

    private String removeAllTags(String input)
    {
        return input.replaceAll("(?i)(<.*?>)(.+?)(</.*?>)", "$2");
    }

    private Course addCourse(ParsingRow row, int semester, Professor professor)
    {
        Course course = courseRepository.getByName(row.courseKey);

        if (course == null)
        {
            Log.e("Course groups", row.groups.get(0));
            course = new Course(row.courseKey, Utils.groupToYear(row.groups.get(0)), semester, row.pack, null);
            course.addProfessor(professor);
            courseRepository.add(course);
        }
        else if (!course.professors.contains(professor.getKey()))
        {
            course.addProfessor(professor);
            courseRepository.update(course);
        }

        return course;
    }

    private void addScheduleClass(ScheduleClass scheduleClass, Professor professor)
    {
        ScheduleClass existingSchedule = scheduleRepository.getById(scheduleClass.getKey());
        scheduleClass.addProfessor(professor);

        if (existingSchedule != null && !existingSchedule.professorKeys.contains(professor.getKey()))
        {
            existingSchedule.addProfessor(professor);
            scheduleRepository.update(existingSchedule);
        }
        else if (existingSchedule == null)
            scheduleRepository.add(scheduleClass);
    }

    private void addExam(Exam exam, Professor professor)
    {
        Exam existingExam = examRepository.getById(exam.getKey());
        exam.addProfessor(professor);

        if (existingExam != null && !existingExam.professorKeys.contains(professor.getKey()))
        {
            existingExam.addProfessor(professor);
            examRepository.update(existingExam);
        }
        else if (existingExam == null)
            examRepository.add(exam);
    }

    private class ParserThread extends Thread
    {
        @Override
        public void run()
        {
            String readResult = readUrl(urlString);

            if(readResult.length() <= 0)
                return;

            List<Professor> professors = parseProfessors(readResult);

            for(Professor professor : professors)
            {
                Professor existingProfessor = professorRepository.getById(professor.getKey());

                if(existingProfessor != null && !existingProfessor.equals(professor))
                    professorRepository.update(professor);
                else if (existingProfessor == null)
                    professorRepository.add(professor);

                List<ParsingRow> parsingRows = parseTable(readUrl(professor.scheduleLink));

                for(ParsingRow row : parsingRows)
                {
                    Course course = addCourse(row, 2, professor);
                    row.courseKey = course.getKey();

                    if(row.type.equals("Examen"))
                    {
                        addExam(row.toExam(), professor);
                    }
                    else
                    {
                        addScheduleClass(row.toScheduleClass(), professor);
                    }
                }
            }

            professorRepository.update(professors);
        }
    }

    private class ValueLinkPair
    {
        String value = "";
        String link = "";

        ValueLinkPair(String tdString)
        {
            boolean hasLink=false;
            String patternWithLink = "(?i)<a.*?href=\"(.*?)\".*?>(.+?)</a>";

            Pattern pattern = Pattern.compile(patternWithLink);
            Matcher matcher = pattern.matcher(tdString);

            StringBuilder linkBuilder = new StringBuilder(), valueBuilder = new StringBuilder();

            while(matcher.find())
            {
                hasLink = true;
                linkBuilder.append(tdString.substring(matcher.start(1), matcher.end(1)).trim()).append(",");
                valueBuilder.append(tdString.substring(matcher.start(2), matcher.end(2)).trim()).append(",");
            }

            if(!hasLink)
                value = tdString.trim().replace("&nbsp;", "None");
            else
            {
                link = linkBuilder.substring(0, linkBuilder.length()-1);
                value = valueBuilder.substring(0, valueBuilder.length()-1);
            }
        }

        @Override
        @NonNull
        public String toString()
        {
            if (link == null)
                return "Value '" + value + "'";
            return "Value: '" + value + "', Link: '" + link + "'";
        }

        Professor toProfessor()
        {
            String firstName = parseFirstName(value);
            String lastName = parseLastName(value);
            String rank = parseRank(value);

            Professor professor = new Professor(firstName, lastName, generateEmail(firstName, lastName), "0742664239", rank, null);
            professor.scheduleLink = "https://profs.info.uaic.ro/~orar/" + link;

            return  professor;
        }

        private String generateEmail(String firstName, String lastName)
        {
            if (firstName.length() > 0)
                return firstName.substring(0,1) + lastName + "@gmail.com";
            else
                return lastName + "@gmail.com";
        }

        private String parseFirstName(String completeString)
        {
            String fullName = completeString.split(",")[0];

            if(fullName.contains(" "))
                return fullName.split(" ")[1];
            return "";
        }

        private String parseLastName(String completeString)
        {
            return completeString.split(" ")[0];
        }

        private String parseRank(String completeString)
        {
            if(!completeString.contains(","))
                return "";
            return completeString.split(",")[1].trim();
        }
    }

    private class ParsingRow
    {
        List<String> rooms;
        String date;
        int day;
        int startHour;
        int endHour;
        String courseKey;
        String type;
        List<String> professorKeys = null;
        String parity = "";
        int pack = 0;
        List<String> groups;

        ParsingRow(List<ValueLinkPair> pairs, int day, String date)
        {
            this.date = date;
            this.day = day;
            startHour = Integer.parseInt(pairs.get(0).value.replace(":",""));
            endHour = Integer.parseInt(pairs.get(1).value.replace(":",""));
            courseKey = pairs.get(2).value.replace("/","&");
            type = pairs.get(3).value;
            groups = Arrays.asList(pairs.get(4).value.split(","));
            rooms = Arrays.asList(pairs.get(5).value.split(","));

            if(pairs.size() > 6)
                parity = pairs.get(6).value;
            if(pairs.size() == 8 && !pairs.get(7).value.equals("None"))
                pack = Integer.parseInt(pairs.get(7).value);
        }

        ScheduleClass toScheduleClass()
        {
            return new ScheduleClass(
                    rooms.get(0),
                    day,
                    startHour,
                    endHour,
                    courseKey,type,
                    professorKeys,
                    parity,
                    pack,
                    groups);
        }

        Exam toExam()
        {
            return new Exam(
                    rooms,
                    day,
                    date,
                    startHour,
                    endHour,
                    courseKey,
                    type,
                    professorKeys,
                    pack,
                    groups);
        }
    }
}
