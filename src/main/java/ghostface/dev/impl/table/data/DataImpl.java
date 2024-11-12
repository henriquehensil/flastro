package ghostface.dev.impl.table.data;

import ghostface.dev.providers.AbstractData;
import ghostface.dev.table.Table;
import ghostface.dev.table.column.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public final class DataImpl extends AbstractData {

    public DataImpl(@NotNull Table table) {
        super(table);
    }

    public boolean hasValue(@Nullable Object object) {
        if (object instanceof CharSequence) {
            @NotNull String str = object.toString();
            return getValues().stream()
                    .filter(CharSequence.class::isInstance)
                    .map(CharSequence.class::cast)
                    .anyMatch(obj -> str.equalsIgnoreCase(obj.toString()));
        } else {
            return getValues().stream().anyMatch(obj -> Objects.equals(obj, object));
        }
    }

    @NotNull Map<@NotNull Column<?>, @Nullable Object> getValueMap() {
        return values;
    }
}
