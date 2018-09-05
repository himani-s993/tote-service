package com.demo.tote.dto;

public class Bet {
    private String product;
    private String selection;
    private double stake;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public double getStake() {
        return stake;
    }

    public void setStake(double stake) {
        this.stake = stake;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bet bet = (Bet) o;

        if (Double.compare(bet.stake, stake) != 0) return false;
        if (product != null ? !product.equals(bet.product) : bet.product != null) return false;
        return selection != null ? selection.equals(bet.selection) : bet.selection == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = product != null ? product.hashCode() : 0;
        result = 31 * result + (selection != null ? selection.hashCode() : 0);
        temp = Double.doubleToLongBits(stake);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
