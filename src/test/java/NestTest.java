
import ghostface.dev.DataType;
import ghostface.dev.exception.NameAlreadyExists;
import ghostface.dev.impl.NestImpl;
import ghostface.dev.nest.Nest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class NestTest {

    @Test
    @DisplayName("Nest operations")
    public void test() {
        final @NotNull Nest<String> nest = new NestImpl<>(DataType.STRING);

        // Checking if the collection is initially empty
        Assertions.assertTrue(nest.toCollection().isEmpty());

        // Inserting values
        Assertions.assertTrue(nest.put("test", "hello world"));
        Assertions.assertTrue(nest.put("test1", "hello java"));
        Assertions.assertFalse(nest.put("test1", "ops")); // Should not allow duplicates with the same key

        // Verifying the presence of values
        Assertions.assertTrue(nest.getValue("test").isPresent());
        Assertions.assertTrue(nest.getValue("test1").isPresent());
        Assertions.assertFalse(nest.getValue("unknown").isPresent()); // Non-existing value

        // Creating a sub-nest
        @NotNull Nest<String> messagesNest = nest.createSub("messages", DataType.STRING);
        Assertions.assertTrue(nest.getSub("messages", DataType.STRING).isPresent());
        Assertions.assertEquals(String.class, nest.getSub("messages", DataType.STRING).get().getDataType().getType());
        Assertions.assertFalse(nest.getSub("messages", DataType.BOOLEAN).isPresent()); // Incorrect type

        // Attempting to create sub-nests with the same name
        try {
            nest.createSub("messages", DataType.INTEGER);
            Assertions.fail("Expected NameAlreadyExists to be thrown");
        } catch (NameAlreadyExists e) {
            Assertions.assertTrue(true);
        }

        // Creating a sub-nest within a sub-nest
        nest.getSub("messages", DataType.STRING).get().createSub("imgMsg", DataType.STRING);
        Assertions.assertTrue(nest.getSub("messages", DataType.STRING).get().getSub("imgMsg", DataType.STRING).isPresent());

        // Attempt to create a duplicate sub-nest
        try {
            messagesNest.createSub("imgMsg", DataType.SHORT);
            Assertions.fail("Expected NameAlreadyExists to be thrown");
        } catch (NameAlreadyExists e) {
            Assertions.assertTrue(true);
        }
    }
}
