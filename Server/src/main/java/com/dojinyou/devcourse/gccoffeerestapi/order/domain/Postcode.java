package com.dojinyou.devcourse.gccoffeerestapi.order.domain;

import java.util.Objects;

public record Postcode(String value) {
    public Postcode {
        valid(value);
    }

    private void valid(String value) {
        if (value == null) {
            throw new IllegalArgumentException("우편번호는 빈 값일 수 없습니다.");
        }
        if (!checkValue(value)) {
            throw new IllegalArgumentException("우편번호는 5자리 숫자여야 합니다.");
        }
    }

    private boolean checkValue(String value) {
        return value.length() == 5 && value.chars().allMatch(Character::isDigit);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Postcode postcode = (Postcode) other;
        return value.equals(postcode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
