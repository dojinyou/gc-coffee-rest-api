package com.dojinyou.devcourse.gccoffeerestapi.order.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String address) {
    private static final int ADDRESS_MIN_LENGTH = 4;
    private static final int ADDRESS_MAX_LENGTH = 50;

    public Email {
        valid(address);
    }

    private static void valid(String address) {
        if (address == null) {
            throw new IllegalArgumentException("이메일 주소는 빈 값일 수 없습니다.");
        }
        if (address.length() < ADDRESS_MIN_LENGTH || ADDRESS_MAX_LENGTH < address.length()) {
            throw new IllegalArgumentException("이메일 주소는 " +
                                                       ADDRESS_MIN_LENGTH + " 이상, " +
                                                       ADDRESS_MAX_LENGTH + " 이하여야 합니다.");
        }
        if (!checkAddress(address)) {
            throw new IllegalArgumentException("이메일 주소가 양식에 맞지 않습니다.");
        }
    }

    private static boolean checkAddress(String address) {
        return Pattern.matches("\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b", address);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Email email = (Email) other;
        return address.equals(email.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
