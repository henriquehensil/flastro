import ghostface.dev.datatype.DataType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class DataTypeTest {

    @Test
    @DisplayName("Tests all datatype providers")
    public void test() {
        Assertions.assertTrue(DataType.BOOLEAN.equalsClass(boolean.class));
        Assertions.assertTrue(DataType.STRING.equalsClass(String.class));
        Assertions.assertTrue(DataType.INTEGER.equalsClass(int.class));
        Assertions.assertTrue(DataType.LONG.equalsClass(long.class));
        Assertions.assertTrue(DataType.FLOAT.equalsClass(float.class));
        Assertions.assertTrue(DataType.DOUBLE.equalsClass(double.class));
        Assertions.assertTrue(DataType.CHAR.equalsClass(char.class));
        Assertions.assertTrue(DataType.BYTE_ARRAY.equalsClass(byte[].class));

        Assertions.assertTrue(DataType.INTEGER.equalsClass(Integer.class));
        Assertions.assertTrue(DataType.LONG.equalsClass(Long.class));
        Assertions.assertTrue(DataType.FLOAT.equalsClass(Float.class));
    }

}
