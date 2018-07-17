package osgi.component.configuration.other;

public @interface MessageConfig {
    String message() default "";
    int iteration() default 0;
}