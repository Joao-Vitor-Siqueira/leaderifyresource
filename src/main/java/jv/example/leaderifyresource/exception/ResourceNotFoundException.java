package jv.example.leaderifyresource.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private String resource;

    public ResourceNotFoundException(String resource) {
        super(STR."\{resource} was not found");
        this.resource = resource;
    }
}
