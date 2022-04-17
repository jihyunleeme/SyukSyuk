package org.devgraft.auth.provider;

import java.time.LocalDateTime;

public class StubLocalDateTimeProvider extends LocalDateTimeProvider {
    public LocalDateTime now = LocalDateTime.now();
    @Override
    public LocalDateTime now() {
        return this.now;
    }
}
