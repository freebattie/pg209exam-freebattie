package no.kristiania.socialbuzz.dao;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static no.kristiania.socialbuzz.dao.DaoMessage.getStringOfDateLastRead;
import static no.kristiania.socialbuzz.dao.DaoMessage.getStringOfDateMessage;
import static org.assertj.core.api.Assertions.assertThat;

public class DaoMessageEpochTest {

    long time = Instant.now().truncatedTo(ChronoUnit.DAYS).getEpochSecond();

    @Test
    public void getStringOfDateMessageTest() {
        var today = getStringOfDateMessage(time);
        var yesterday = getStringOfDateMessage(time - 86_400);
        var underYear = getStringOfDateMessage(time - 259200);
        var overYear = getStringOfDateMessage(time - 31_536_000);

        var localDateTime = LocalDateTime.now();
        var localDateUnder = localDateTime.minusDays(3);
        var localDateOver = localDateTime.minusYears(1);

        var formatUnderYear = DateTimeFormatter.ofPattern("d MMM");
        var stringUnderYear = localDateUnder.format(formatUnderYear);

        var formatOverYear = DateTimeFormatter.ofPattern("d MMM uuuu");
        var stringOverYear = localDateOver.format(formatOverYear);


        assertThat(today)
                .as("Test Today *time*")
                .contains("Today 01:00");

        assertThat(yesterday)
                .as("Test Yesterday *time*")
                .contains("Yesterday 01:00");

        assertThat(underYear)
                .as("Test 3 days ago *time*")
                .contains(stringUnderYear+" at 01:00");

        assertThat(overYear)
                .as("Test one year ago *time*")
                .contains(stringOverYear+" at 01:00");
    }

    @Test
    public void getStringOfDateLastReadTest() {

        var now = Instant.now().getEpochSecond();

        var secondsAgo = now - 1;
        var minutesAgo = now - 60;
        var hoursAgo = now - 3_600;
        var daysAgo = now - 86_400;
        var weeksAgo = now - 604_800;
        var monthsAgo = now - 2_592_000;
        var yearsAgo = now - 31_536_000;

        var stringSeconds = getStringOfDateLastRead(secondsAgo);
        var stringMinutes = getStringOfDateLastRead(minutesAgo);
        var stringHours = getStringOfDateLastRead(hoursAgo);
        var stringDays = getStringOfDateLastRead(daysAgo);
        var stringWeeks = getStringOfDateLastRead(weeksAgo);
        var stringMonths = getStringOfDateLastRead(monthsAgo);
        var stringYears = getStringOfDateLastRead(yearsAgo);

        assertThat(stringSeconds)
                .as("Test seconds")
                .contains("seconds");

        assertThat(stringMinutes)
                .as("Test minutes")
                .contains("minutes");

        assertThat(stringHours)
                .as("Test hours")
                .contains("hours");

        assertThat(stringDays)
                .as("Test days")
                .contains("days");

        assertThat(stringWeeks)
                .as("Test weeks")
                .contains("weeks");

        assertThat(stringMonths)
                .as("Test months")
                .contains("months");

        assertThat(stringYears)
                .as("Test years")
                .contains("years");
    }

}
