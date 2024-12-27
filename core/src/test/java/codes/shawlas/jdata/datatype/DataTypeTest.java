package codes.shawlas.jdata.datatype;

import codes.shawlas.jdata.core.DataType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.*;

public final class DataTypeTest {

    @Order(0)
    private static @NotNull InputStream getInputStream() throws IOException {
        @NotNull ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        @NotNull DataOutputStream output = new DataOutputStream(byteStream);

        // Strings
        output.writeUTF("hello world");
        output.writeUTF("java");
        // Booleans
        output.writeBoolean(false);
        output.writeBoolean(true);
        // Integers
        output.writeInt(555);
        output.writeInt(777);
        // bytes
        output.writeByte(57);
        output.writeByte(58);
        // shorts
        output.writeShort(9);
        output.writeShort(8);
        // chars
        output.writeChar('h');
        output.writeChar('j');
        // doubles
        output.writeDouble(99.99);
        output.writeDouble(50.54);
        // floats
        output.writeFloat(8.5F);
        output.writeFloat(9.0F);
        // long
        output.writeLong(6519825612986576L);
        output.writeLong(1234553678689096576L);
        return new ByteArrayInputStream(byteStream.toByteArray());
    }

    @Test
    @Order(1)
    @DisplayName("tests the primitives data type")
    public void primitives() throws IOException {

        @NotNull InputStream inputStream = getInputStream();

        // String
        @NotNull String str = DataType.STRING.read(inputStream);
        Assertions.assertEquals("hello world", str);
        str = DataType.STRING.read(inputStream);
        Assertions.assertEquals("java", str);

        // Boolean
        boolean b = DataType.BOOLEAN.read(inputStream);
        Assertions.assertFalse(b);
        b = DataType.BOOLEAN.read(inputStream);
        Assertions.assertTrue(b);

        // Integer
        int integer = DataType.INTEGER.read(inputStream);
        Assertions.assertEquals(555, integer);
        integer = DataType.INTEGER.read(inputStream);
        Assertions.assertEquals(777, integer);

        // Byte
        byte bt = DataType.BYTE.read(inputStream);
        Assertions.assertEquals(57, bt);
        bt = DataType.BYTE.read(inputStream);
        Assertions.assertEquals(58, bt);

        // Short
        short s = DataType.SHORT.read(inputStream);
        Assertions.assertEquals(9, s);
        s = DataType.SHORT.read(inputStream);
        Assertions.assertEquals(8, s);

        // Char
        char ch = DataType.CHAR.read(inputStream);
        Assertions.assertEquals('h', ch);
        ch = DataType.CHAR.read(inputStream);
        Assertions.assertEquals('j', ch);

        // Double
        double db = DataType.DOUBLE.read(inputStream);
        Assertions.assertEquals(99.99, db);
        db = DataType.DOUBLE.read(inputStream);
        Assertions.assertEquals(50.54, db);

        // Float
        float ft = DataType.FLOAT.read(inputStream);
        Assertions.assertEquals(8.5F, ft);
        ft = DataType.FLOAT.read(inputStream);
        Assertions.assertEquals(9.0F, ft);

        long l = DataType.LONG.read(inputStream);
        Assertions.assertEquals(6519825612986576L, l);
        l = DataType.LONG.read(inputStream);
        Assertions.assertEquals(1234553678689096576L, l);
    }
}
