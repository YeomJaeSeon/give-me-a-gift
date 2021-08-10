package myProject1.gift.test;

import myProject1.gift.domain.Role;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class Test {
    public static void main(String[] args) {

        LocalDate localDate = LocalDate.now();
        LocalDate localDate1 = LocalDate.of(1996, 3, 7);
        LocalDate localDate2 = LocalDate.of(1996, 3, 7);
        System.out.println("(localDate1 == localDate2) = " + (localDate1 == localDate2));
        System.out.println(localDate1.get(ChronoField.YEAR) == localDate2.get(ChronoField.YEAR));
    }
}
