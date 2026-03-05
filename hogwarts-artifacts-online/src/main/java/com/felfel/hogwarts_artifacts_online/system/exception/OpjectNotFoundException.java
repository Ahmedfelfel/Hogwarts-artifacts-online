package com.felfel.hogwarts_artifacts_online.system.exception;

public class OpjectNotFoundException extends RuntimeException {
    public OpjectNotFoundException(String objectType,String Id) {
        super("Could not find "+objectType+" with ID " + Id + " :(");
    }
    public OpjectNotFoundException(String objectType,Integer Id) {
        super("Could not find "+objectType+" with ID " + Id + " :(");
    }
    public OpjectNotFoundException(String objectType)
    {
        super(("Could not find any "+objectType+" :("));
    }
}
