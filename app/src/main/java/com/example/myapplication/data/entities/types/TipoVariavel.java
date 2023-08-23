package com.example.myapplication.data.entities.types;

public enum TipoVariavel {

    TIME(0){
        @Override
        public String toString() {
            return "TIME";
        }
    },

    STRING(1){
        @Override
        public String toString() {
            return "STRING";
        }
    },

    VALOR(2){
        @Override
        public String toString() {
            return "VALOR";
        }
    };

    public final int value;

    TipoVariavel(final int value){
        this.value = value;
    }
    public static TipoVariavel valueOf(int value) throws IllegalArgumentException{
        for (TipoVariavel lado : TipoVariavel.values()) {
            if(value == lado.value)
                return lado;
        }

        throw new IllegalArgumentException("Não existe tipo informado.");
    }
}
