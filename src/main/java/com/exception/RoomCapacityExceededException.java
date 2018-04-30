package com.exception;

public class RoomCapacityExceededException extends Exception {
    public RoomCapacityExceededException(String message) {
        super(message);
    }
}
