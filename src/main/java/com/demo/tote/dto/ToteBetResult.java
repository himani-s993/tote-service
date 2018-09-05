package com.demo.tote.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ToteBetResult implements Serializable {
    private static final long serialVersionUID = -2851763159833437124L;

    private List<String> dividends;

    public List<String> getDividends() {
        return dividends;
    }

    public void setDividends(List<String> dividends) {
        this.dividends = dividends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToteBetResult that = (ToteBetResult) o;
        return Objects.equals(dividends, that.dividends);
    }

    @Override
    public int hashCode() {

        return Objects.hash(dividends);
    }
}
