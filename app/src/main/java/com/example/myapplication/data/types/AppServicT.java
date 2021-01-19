package com.example.myapplication.data.types;

public enum AppServicT {

    PADRAO(0){
        @Override
        public String toString() {
            return TipoServico.PADRAO;
        }
    },

    PRODUCAO(1){
        @Override
        public String toString() {
            return TipoServico.PRODUCAO;
        }
    },

    PRODUCAO_INDIVIDUAL(2){
        @Override
        public String toString() {
            return TipoServico.PRODUCAO_INDIVIDUAL;
        }
    },

    NENHUM(3){
        @Override
        public String toString() {
            return TipoServico.NENHUM;
        }
    };

    public final int value;

    AppServicT(int value){
        this.value = value;
    }

    public static AppServicT valueOf(int value){
        for (AppServicT appUsage : AppServicT.values()) {
            if(appUsage.value == value)
                return appUsage;
        }

        throw new IllegalArgumentException("Não existe um resultado no enum de AppServicT para o valor informado.");
    }

}
