package com.hyeonjs.cryptocurrencyinfo;

public class CoinInfo implements Comparable<CoinInfo> {

    public final String name, mark;

    public CoinInfo(String name, String mark) {
        this.name = name;
        this.mark = mark;
    }

    @Override
    public int compareTo(CoinInfo other) {
        return name.compareTo(other.name);
    }

}
