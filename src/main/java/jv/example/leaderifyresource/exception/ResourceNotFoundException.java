package jv.example.leaderifyresource.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private String resource;

    public ResourceNotFoundException(String resource, Long id) {
        super(STR."\{resource} with id \{id} was not found");
        this.resource = resource;
    }
}
