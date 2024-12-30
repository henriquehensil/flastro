package codes.shawlas.data.input;

import codes.shawlas.data.Message;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public interface InputMessage extends Message {

    @NotNull JsonObject getData();

}
