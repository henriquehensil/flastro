package ghostface.dev.table;

import ghostface.dev.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface DatabaseTable extends Table {

    @Override
    @NotNull Optional<Data> getData(int row);

}
