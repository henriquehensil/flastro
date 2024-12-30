import codes.shawlas.data.MessageModel;
import codes.shawlas.data.exception.message.MessageParseException;
import codes.shawlas.data.message.MessageStructure;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class MessageTests {

    @NotNull String @NotNull [] valid = {
            "GET FILE\r\nIDENTIFIER: 12\r\n\r\n{\"path\": \"/root/test.json\"}",
            "CREATE COLUMN\r\nIDENTIFIER: 32\r\n\r\n{\"table\": \"6\", \"name\": \"test\"}",
            "DELETE NEST\r\nIDENTIFIER: 54\r\n\r\n{\"father\": \"null\", \"id\": \"4756\"}",
            "UPDATE ELEMENT\r\nIDENTIFIER: 24\r\n\r\n{\"table\": \"2\", \"element\": \"254\", \"value\": \"test\", \"column\": \"nickname\"}"
    };

    @NotNull String @NotNull [] invalid = {
            "TAKE FILE\r\nIDENTIFIER: 12\r\n\r\n{\"path\": \"/root/test.json\"}",
            "NEW COLUMN\r\nIDENTIFIER: 32\r\n\r\n{\"table\": \"6\", \"name\": \"test\"}",
            "REMOVE NEST\r\nIDENTIFIER: 54\r\n\r\n{\"father\": \"null\", \"id\": \"4756\"}",
            "SET ELEMENT\r\nIDENTIFIER: 24\r\n\r\n{\"table\": \"2\", \"element\": \"254\", \"value\": \"test\", \"column\": \"nickname\"}",

            "GET FILE\r\nIDENTIFIER: -12\r\n\r\n{\"path\": \"/root/test.json\"}",
            "CREATE COLUMN\r\nIDENTIFIER: -32\r\n\r\n{\"table\": \"6\", \"name\": \"test\"}",
            "DELETE NEST\r\nIDENTIFIER: -54\r\n\r\n{\"father\": \"null\", \"id\": \"4756\"}",
            "UPDATE ELEMENT\r\nIDENTIFIER: -24\r\n\r\n{\"table\": \"2\", \"element\": \"254\", \"value\": \"test\", \"column\": \"nickname\"}",
    };

    @NotNull String @NotNull [] invalidJsons = {
            "GET FILE\r\nIDENTIFIER: 12\r\n\r\n{\"path\" \"/root/test.json\"}",
            "CREATE COLUMN\r\nIDENTIFIER: 32\r\n\r\n{table \"6\", \"name\": \"test\"}",
            "DELETE NEST\r\nIDENTIFIER: 54\r\n\r\n{\"father\": \"null\", \"id\": \"4756\"",
            "UPDATE ELEMENT\r\nIDENTIFIER: 24\r\n\r\n{\"table\": \"2\" \"element\": \"254\" \"value\": \"test\", \"column\": \"nickname\"}"
    };

    @Test
    public void Validations() {
        for (@NotNull String valid : valid) {
            Assertions.assertTrue(MessageStructure.isValid(valid));
            Assertions.assertTrue(MessageStructure.isValidHeader(valid.split("\r\n\r\n")[0]));
            Assertions.assertDoesNotThrow(() -> MessageStructure.parse(valid));
            Assertions.assertDoesNotThrow(() -> MessageStructure.parse(valid.split("\r\n\r\n")[0], new JsonObject()));
        }

        for (@NotNull String invalid : invalid) {
            Assertions.assertFalse(MessageStructure.isValid(invalid));
            Assertions.assertFalse(MessageStructure.isValidHeader(invalid.split("\r\n\r\n")[0]));
            Assertions.assertThrows(MessageParseException.class, () -> MessageStructure.parse(invalid));
            Assertions.assertThrows(MessageParseException.class, () -> MessageStructure.parse(invalid.split("\r\n\r\n")[0], new JsonObject()));
        }

        for (@NotNull String invalid : invalidJsons) {
            Assertions.assertFalse(MessageStructure.isValid(invalid));
            Assertions.assertThrows(MessageParseException.class, () -> MessageStructure.parse(invalid));
        }
    }
}