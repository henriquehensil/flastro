package codes.shawlas.data.message.annotation;

import codes.shawlas.data.message.Message;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MessageDependency {
    @NotNull Class<? extends Message.Input> value();
}