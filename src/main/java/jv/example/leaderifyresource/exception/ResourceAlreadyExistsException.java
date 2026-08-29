package jv.example.leaderifyresource.exception;

import lombok.Getter;

@Getter
public class ResourceAlreadyExistsException extends RuntimeException {
    private final String resource;
    public ResourceAlreadyExistsException(String resource) {
        super(STR."\{resource} already exists");
        this.resource = resource;
    }
}
