package mc.skyblock.plugin.configuration.annotation;

import java.lang.annotation.Retention;


@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface TypeAdapterType {

    Class<?> value();

}
