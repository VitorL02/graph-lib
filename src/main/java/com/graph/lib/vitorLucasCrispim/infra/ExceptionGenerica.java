package com.graph.lib.vitorLucasCrispim.infra;



public class ExceptionGenerica extends RuntimeException  {

    public ExceptionGenerica(String message){
        super(message);
    }

    public ExceptionGenerica(String message,Throwable cause) {
        super(message,cause);
    }


}