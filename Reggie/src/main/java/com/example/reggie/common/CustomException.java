package com.example.reggie.common;

public class CustomException extends RuntimeException{

    public CustomException(){}
    public CustomException(String message){
        super(message);
    };
}
