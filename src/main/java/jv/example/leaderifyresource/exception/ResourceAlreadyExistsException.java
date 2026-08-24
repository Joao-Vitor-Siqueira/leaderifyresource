package jv.example.leaderifyresource.exception;

import lombok.Getter;

@Getter
public class ResourceAlreadyExistsException extends RuntimeException {
    private String resource;
    public ResourceAlreadyExistsException(String resource, Long id) {
        super(STR."\{resource} with id \{id} already exists");
        this.resource = resource;
    }
}
