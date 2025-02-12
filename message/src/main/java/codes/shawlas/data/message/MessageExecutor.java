package codes.shawlas.data.message;

import codes.shawlas.data.exception.MessageExecutionException;

public interface MessageExecutor {

    void execute() throws MessageExecutionException;

}