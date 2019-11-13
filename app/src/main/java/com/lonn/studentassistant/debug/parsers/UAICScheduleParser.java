package com.lonn.studentassistant.debug.parsers;

public class UAICScheduleParser {
    public void parseUAICSchedule() {
        new ParsingThread().start();
    }

    private class ParsingThread extends Thread {
        @Override
        public void run() {
            new ProfessorsPageParser().parse();
        }
    }
}
