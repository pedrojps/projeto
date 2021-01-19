package com.example.myapplication.data.types;

public enum AppEnvironment {

    CONSTRUCAO(0){
        @Override
        public String toString() {
            return "CONSTRUCAO";
        }
    },

    COLETA_LIXO(1){
        @Override
        public String toString() {
            return "COLETA_LIXO";
        }
    },

    FAZENDA(2){
        @Override
        public String toString() {
            return "FAZENDA";
        }
    };

    public final int value;

    AppEnvironment(int value){
        this.value = value;
    }

    public static AppEnvironment valueOf(int value){
        for (AppEnvironment appUsage : AppEnvironment.values()) {
            if(appUsage.value == value)
                return appUsage;
        }

        throw new IllegalArgumentException("Não existe um resultado no enum de AppEnvironment para o valor informado.");
    }

}
