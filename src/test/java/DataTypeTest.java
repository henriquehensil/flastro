import ghostface.dev.datatype.DataType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class DataTypeTest {

    @Test
    @DisplayName("Tests all datatype providers")
    public void test() {
        Assertions.assertTrue(DataType.BOOLEAN.equalsType(boolean.class));
        Assertions.assertTrue(DataType.STRING.equalsType(String.class));
        Assertions.assertTrue(DataType.INTEGER.equalsType(int.class));
        Assertions.assertTrue(DataType.LONG.equalsType(long.class));
        Assertions.assertTrue(DataType.FLOAT.equalsType(float.class));
        Assertions.assertTrue(DataType.DOUBLE.equalsType(double.class));
        Assertions.assertTrue(DataType.CHAR.equalsType(char.class));
        Assertions.assertTrue(DataType.BYTE_ARRAY.equalsType(byte[].class));

        Assertions.assertTrue(DataType.INTEGER.equalsType(Integer.class));
        Assertions.assertTrue(DataType.LONG.equalsType(Long.class));
        Assertions.assertTrue(DataType.FLOAT.equalsType(Float.class));
    }

}
