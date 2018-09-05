package com.demo.tote.dto;

import java.io.Serializable;

public class Result implements Serializable {
    private static final long serialVersionUID = 8422917906942342577L;

    private int firstPosition;
    private int secondPosition;
    private int thirdPosition;

    public int getFirstPosition() {
        return firstPosition;
    }

    public void setFirstPosition(int firstPosition) {
        this.firstPosition = firstPosition;
    }

    public int getSecondPosition() {
        return secondPosition;
    }

    public void setSecondPosition(int secondPosition) {
        this.secondPosition = secondPosition;
    }

    public int getThirdPosition() {
        return thirdPosition;
    }

    public void setThirdPosition(int thirdPosition) {
        this.thirdPosition = thirdPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result result = (Result) o;

        if (firstPosition != result.firstPosition) return false;
        if (secondPosition != result.secondPosition) return false;
        return thirdPosition == result.thirdPosition;
    }

    @Override
    public int hashCode() {
        int result = firstPosition;
        result = 31 * result + secondPosition;
        result = 31 * result + thirdPosition;
        return result;
    }
}
