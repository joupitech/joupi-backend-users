package joupi.exception

/**
 * Exception thrown when attempting to create a user that already exists
 */
class UserAlreadyExistsException(message: String) : RuntimeException(message) 