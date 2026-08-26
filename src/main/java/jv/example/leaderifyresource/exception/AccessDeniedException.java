package jv.example.leaderifyresource.exception;

import lombok.Getter;

@Getter
public class AccessDeniedException extends RuntimeException {
    private String resource;
    public AccessDeniedException(String resource) {
        super(STR."User is not authorized to access \{resource}");
        this.resource = resource;
    }
}
