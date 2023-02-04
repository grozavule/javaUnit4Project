package dev.ericdrake.notepad.exceptions;

public class UserAlreadyExistsException extends Exception {
    public static final String ERROR = "The username that you entered is already in use. " +
            "Please choose a different username";
    public UserAlreadyExistsException(String message){
        super(message);
    }
}
