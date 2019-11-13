package com.lonn.studentassistant.debug.parsers;

import android.util.Log;

import com.lonn.studentassistant.firebaselayer.models.Professor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

class ProfessorsPageParser {
    private static final String PROFESSORS_PAGE = "https://profs.info.uaic.ro/~orar/orar_profesori.html";

    List<Professor> parse() {
        Document doc;

        try {
            doc = Jsoup.connect(PROFESSORS_PAGE).get();
            Log.i("Connected to", PROFESSORS_PAGE);
        } catch (IOException e) {
            if (e.getMessage() != null) {
                Log.e("Error parsing schedule", e.getMessage());
            }
            return null;
        }

        Log.i("Parsing", doc.title());

        List<Professor> professors = new LinkedList<>();
        Elements htmlProfessorListItems = doc.select("li");

        for (Element htmlProfessorListItem : htmlProfessorListItems) {
            Element anchorProfessorItem = htmlProfessorListItem.selectFirst("a");
            professors.add(parseProfessor(anchorProfessorItem));
        }

        return professors;
    }

    private Professor parseProfessor(Element anchorProfessorItem) {
        String anchorText = anchorProfessorItem.text();
        String anchorLink = anchorProfessorItem.attr("abs:href");

        String[] nameRankSplit = anchorText.split(",");
        String professorRank = null;

        if (nameRankSplit.length > 1) {
            professorRank = anchorText.split(",")[1]
                    .trim();
        }

        String[] nameSplit = nameRankSplit[0].split(" ", 2);

        String professorFirstName = null;

        if (nameSplit.length > 1) {
            professorFirstName = nameSplit[1];
        }
        String professorLastName = nameSplit[0];

        return new Professor().setFirstName(professorFirstName)
                .setLastName(professorLastName)
                .setLevel(professorRank)
                .setScheduleLink(anchorLink)
                .setEmail(generateEmailForName(professorFirstName, professorLastName))
                .setWebsite(generateWebsiteForName(professorFirstName, professorLastName))
                .setPhoneNumber("0123456789");
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
            lastNamePart = lastName.split(" ")[0].toLowerCase().substring(0,1);
        }

        if (firstName == null && lastName == null) {
            return null;
        }

        return "https://profs.info.uaic.ro/~" +
                lastNamePart +
                firstNamePart;
    }
}
