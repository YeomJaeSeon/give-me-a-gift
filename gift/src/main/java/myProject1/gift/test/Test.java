package myProject1.gift.test;

import myProject1.gift.domain.Role;

public class Test {
    public static void main(String[] args) {
        Role role = Role.ADMIN;
        System.out.println(Role.valueOf("ADMIN"));
    }
}
