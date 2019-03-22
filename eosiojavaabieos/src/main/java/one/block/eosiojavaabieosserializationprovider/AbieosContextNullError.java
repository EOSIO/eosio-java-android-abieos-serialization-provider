package one.block.eosiojavaabieosserializationprovider;

import one.block.eosiojava.error.serializationprovider.SerializationProviderError;
import org.jetbrains.annotations.NotNull;

//
// AbieosContextNullError
// eosio-java-android-abieos-serialization-provider
//
// Created by mccoole on 3/22/19
// Copyright © 2018-2019 block.one.
//

public class AbieosContextNullError extends SerializationProviderError {

    public AbieosContextNullError() {
    }

    public AbieosContextNullError(@NotNull String message) {
        super(message);
    }

    public AbieosContextNullError(@NotNull String message,
            @NotNull Exception exception) {
        super(message, exception);
    }

    public AbieosContextNullError(@NotNull Exception exception) {
        super(exception);
    }
}
