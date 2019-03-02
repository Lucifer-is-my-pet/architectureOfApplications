package main.modules;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ThreadLocalRandom;

/*
* Генерирует дату рождения и высчитывает возраст, исходя из сгенерированного
 */
class Birthdate {

    private String birthdate;
    private String pattern;

    Birthdate(String pattern) {
        this.pattern = pattern;

        DateFormat dateFormat = new SimpleDateFormat(this.pattern);
        GregorianCalendar birthday = new GregorianCalendar();

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        int year = ThreadLocalRandom.current().nextInt(thisYear - 100, thisYear);
        birthday.set(Calendar.YEAR, year);
        int dayOfYear = ThreadLocalRandom.current().nextInt(1, birthday.getActualMaximum(Calendar.DAY_OF_YEAR));
        birthday.set(Calendar.DAY_OF_YEAR, dayOfYear);

        this.birthdate = dateFormat.format(birthday.getTime());
    }

    String get() {
        return this.birthdate;
    }

    long getAge() {
        LocalDate localBirthDate = LocalDate.parse(this.birthdate, DateTimeFormatter.ofPattern(this.pattern));
        LocalDate now = LocalDate.now(ZoneId.systemDefault());

        return (ChronoUnit.YEARS.between(localBirthDate, now));
    }
}
