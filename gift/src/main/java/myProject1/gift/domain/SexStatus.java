package myProject1.gift.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SexStatus {
    MALE("MALE"),
    FEMALE("FEMALE");

    private String value;
}
