package org.example.variaveis;

public class VariaveisDaAplicacao {

    private static boolean chaveDoCaos = false;
    private static boolean chaveDeException = false;

    private static int latencia = 0;

    private VariaveisDaAplicacao() {
    }

    public static boolean alteraEstadoDaChaveDoCaos() {

        if (chaveDoCaos) {
            chaveDoCaos = false;
            chaveDeException = false;
        } else {
            chaveDoCaos = true;
        }
        return chaveDoCaos;
    }

    public static boolean isChaveDoCaos() {
        return chaveDoCaos;
    }

    public static boolean isChaveDeException() {

        if (chaveDeException) {
            chaveDeException = false;
        } else {
            chaveDeException = true;
        }

        return chaveDeException;
    }

    public static long getLatencia() {
        return latencia;
    }

    public static void setLatencia(int latenciaRecebida) {
        latencia = latenciaRecebida;
    }
}
