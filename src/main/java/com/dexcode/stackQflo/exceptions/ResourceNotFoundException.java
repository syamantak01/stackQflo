package com.dexcode.stackQflo.exceptions;

public class ResourceNotFoundException extends RuntimeException{


    public ResourceNotFoundException(String resourceName, String fieldKey, long fieldValue) {
        super(String.format("%s not found with %s: %d", resourceName, fieldKey, fieldValue));
    }
}
