package com.lonn.scheduleparser.parsingServices.professors;

import com.lonn.scheduleparser.parsingServices.abstractions.Mapper;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;
import com.lonn.studentassistant.firebaselayer.models.Professor;

import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfessorMapper extends Mapper<Professor> {
    private static final String FIRST_NAME_REGEX = "(?: ([^,]+))?";
    private static final String LAST_NAME_REGEX = "([^ ,]+)";
    private static final String RANK_REGEX = "(?:,(?: )*(.*)(?: )*)?";
    private static final String ANCHOR_TEXT_REGEX = LAST_NAME_REGEX + FIRST_NAME_REGEX + RANK_REGEX;
    private static final Pattern ANCHOR_TEXT_PATTERN = Pattern.compile(ANCHOR_TEXT_REGEX);

    public Professor map(Element anchorProfessorItem) {
        String firstName = getFirstNameFromAnchor(anchorProfessorItem);
        String lastName = getLastNameFromAnchor(anchorProfessorItem);
        String rank = getRankFromAnchor(anchorProfessorItem);
        String scheduleLink = getProfessorScheduleLinkFromAnchor(anchorProfessorItem);

        return new Professor().setFirstName(firstName)
                .setLastName(lastName)
                .setLevel(rank)
                .setScheduleLink(scheduleLink)
                .setEmail(generateEmailForName(firstName, lastName))
                .setWebsite(generateWebsiteForName(firstName, lastName))
                .setPhoneNumber("0123456789");
    }

    private String getFirstNameFromAnchor(Element anchorElement) {
        return getGroupFromAnchorRegex(anchorElement, 2);
    }

    private String getLastNameFromAnchor(Element anchorElement) {
        return getGroupFromAnchorRegex(anchorElement, 1);
    }

    private String getRankFromAnchor(Element anchorElement) {
        return getGroupFromAnchorRegex(anchorElement, 3);
    }

    private String getProfessorScheduleLinkFromAnchor(Element anchorElement) {
        return anchorElement.attr("abs:href");
    }

    private String generateEmailForName(String firstName, String lastName) {
        String firstNamePart = null;
        String lastNamePart = null;

        if (firstName != null) {
            firstNamePart = firstName.split(" ")[0].toLowerCase();
        }

        if (lastName != null) {
            lastNamePart = lastName.split(" ")[0].toLowerCase();
        }

        if (firstName == null && lastName == null) {
            return null;
        }

        return firstNamePart +
                ((firstName != null && lastName != null) ? "." : "") +
                lastNamePart +
                "@info.uaic.ro";
    }

    private String generateWebsiteForName(String firstName, String lastName) {
        String firstNamePart = null;
        String lastNamePart = null;

        if (firstName != null) {
            firstNamePart = firstName.split(" ")[0].toLowerCase();
        }

        if (lastName != null) {
            lastNamePart = lastName.split(" ")[0].toLowerCase().substring(0, 1);
        }

        if (firstName == null && lastName == null) {
            return null;
        }

        return "https://profs.info.uaic.ro/~" +
                lastNamePart +
                firstNamePart;
    }

    private String getGroupFromAnchorRegex(Element anchorElement, int regexGroup) {
        String anchorText = anchorElement.text();

        Matcher matcher = ANCHOR_TEXT_PATTERN.matcher(anchorText);

        if (matcher.find() && regexGroup <= matcher.groupCount()) {
            return matcher.group(regexGroup);
        }
        return null;
    }
}
