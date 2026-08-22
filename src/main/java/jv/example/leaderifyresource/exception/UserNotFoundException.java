package jv.example.leaderifyresource.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super(STR."User with id \{id} was not found");
    }
}
