package com.felfel.hogwarts_artifacts_online.system.exception;

/**
 * The type Opject not found exception.
 */
public class OpjectNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Opject not found exception.
     *
     * @param objectType the object type
     * @param Id         the id
     */
    public OpjectNotFoundException(String objectType,String Id) {
        super("Could not find "+objectType+" with ID " + Id + " :(");
    }

    /**
     * Instantiates a new Opject not found exception.
     *
     * @param objectType the object type
     * @param Id         the id
     */
    public OpjectNotFoundException(String objectType,Integer Id) {
        super("Could not find "+objectType+" with ID " + Id + " :(");
    }

    /**
     * Instantiates a new Opject not found exception.
     *
     * @param objectType the object type
     */
    public OpjectNotFoundException(String objectType)
    {
        super(("Could not find any "+objectType+" :("));
    }
}
