package com.example.myapplication.data.network;

public enum Status {

    LOADING(0){
        @Override
        public String toString() {
            return "LOADING";
        }
    },
    SUCCESS(1){
        @Override
        public String toString() {
            return "OK";
        }
    },
    ERROR(2){
        @Override
        public String toString() {
            return "ERROR";
        }
    };

    private final int value;

    Status(int value){
        this.value = value;
    }

}
