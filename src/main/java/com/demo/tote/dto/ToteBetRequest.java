package com.demo.tote.dto;

import java.io.Serializable;
import java.util.List;

public class ToteBetRequest implements Serializable {
    private static final long serialVersionUID = 4381020640941826643L;

    private List<String> bets;
    private String raceResult;

    public List<String> getBets() {
        return bets;
    }

    public void setBets(List<String> bets) {
        this.bets = bets;
    }

    public String getRaceResult() {
        return raceResult;
    }

    public void setRaceResult(String raceResult) {
        this.raceResult = raceResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ToteBetRequest toteBetRequest = (ToteBetRequest) o;

        if (bets != null ? !bets.equals(toteBetRequest.bets) : toteBetRequest.bets != null) return false;
        return raceResult != null ? raceResult.equals(toteBetRequest.raceResult) : toteBetRequest.raceResult == null;
    }

    @Override
    public int hashCode() {
        int result = bets != null ? bets.hashCode() : 0;
        result = 31 * result + (raceResult != null ? raceResult.hashCode() : 0);
        return result;
    }
}
