package ru.shulte;

public class Calculation {
    public static  float[] getResult(int[] attemtsTime){
        float[] charachteristics = new float[3];
        charachteristics[0] = (float)(attemtsTime[0] + attemtsTime[1] + attemtsTime[2] + attemtsTime[3] + attemtsTime[4]) / 5;
        charachteristics[1] = attemtsTime[0] / charachteristics[0];
        charachteristics[2] = attemtsTime[3] / charachteristics[0];

        return charachteristics;
    }
}
