package ru.shulte;

import java.sql.Timestamp ;

class Result {
    int resId;
    int userId;
    Timestamp date;
    int[] T = new int[5];
    float jobEfficiency;
    float workabilityDegree;
    float mentalStability;
    Result(int resId, int userId, Timestamp date, int[] T, float jobEfficiency, float workabilityDegree, float mentalStability) {
        this.resId = resId;
        this.userId = userId;
        this.date = date;
        this.T[0] = T[0];
        this.T[1] = T[1];
        this.T[2] = T[2];
        this.T[3] = T[3];
        this.T[4] = T[4];
        this.jobEfficiency = jobEfficiency;
        this.workabilityDegree = workabilityDegree;
        this.mentalStability = mentalStability;
    }
}
