package myProject1.gift.test;

import myProject1.gift.domain.Role;

import java.time.LocalDate;

public class Test {
    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2021, 3, 20);
        System.out.println("date = " + date.getMonthValue());
        System.out.println("date.getDayOfMonth() = " + date.getDayOfMonth());
    }
}
