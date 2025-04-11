package com.example.myapplication;

public class StarData {
    String ra, dec, v, pri, sec, pri_sp, sec_sp, bv, type, firstVar, secondVar, name;

    public StarData(String ra, String dec, String v, String pri, String sec, String pri_sp, String sec_sp,
                    String bv, String type, String firstVar, String secondVar, String name) {
        this.ra = ra;
        this.dec = dec;
        this.v = v;
        this.pri = pri;
        this.sec = sec;
        this.pri_sp = pri_sp;
        this.sec_sp = sec_sp;
        this.bv = bv;
        this.type = type;
        this.firstVar = firstVar;
        this.secondVar = secondVar;
        this.name = name;
    }

    public String getRA() {
        return ra;
    }

    public String getDEC() {
        return dec;
    }

    public String getV() {
        return v;
    }

    public String getPRI() {
        return pri;
    }

    public String getSEC() {
        return sec;
    }

    public String getPRI_SP() {
        return pri_sp;
    }

    public String getSEC_SP() {
        return sec_sp;
    }

    public String getBV() {
        return bv;
    }

    public String getTYPE() {
        return type;
    }

    public String getFIRST_VAR() {
        return firstVar;
    }

    public String getSECOND_VAR() {
        return secondVar;
    }

    public String getNAME() {
        return name;
    }
}
