package codes.shawlas.data.core.nest;

import codes.shawlas.jdata.Context;
import codes.shawlas.data.core.DataType;
import codes.shawlas.jdata.FieldsProviders;
import codes.shawlas.data.core.impl.NestImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public final class NestTests implements Context {
    private static final @NotNull NestImpl<@NotNull String> nest = FieldsProviders.getNestStorage().getNests().create("tests", DataType.STRING);

    @Override
    @Test
    @Order(value = 0)
    public void initialContext() {
        Assertions.assertTrue(nest.getValues().isEmpty());
        Assertions.assertNull(nest.getFather());
        Assertions.assertFalse(nest.getSubs().exists(UUID.randomUUID().toString()));
        Assertions.assertTrue(nest.getSubs().toCollection().isEmpty());
        @NotNull UUID id = UUID.fromString(nest.getId());
        Assertions.assertFalse(nest.getSubs().exists(id.toString()));
    }
}
