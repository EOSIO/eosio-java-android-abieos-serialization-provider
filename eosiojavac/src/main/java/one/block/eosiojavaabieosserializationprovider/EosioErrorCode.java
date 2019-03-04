package one.block.eosiojavaabieosserializationprovider;

public enum EosioErrorCode {
    biometricsDisabled("biometricsDisabled"),
    keychainError("keychainError"),
    manifestError("manifestError"),
    metadataError("metadataError"),
    networkError("networkError"),
    parsingError("parsingError"),
    resourceIntegrityError("resourceIntegrityError"),
    resourceRetrievalError("resourceRetrievalError"),
    signingError("signingError"),
    transactionError("transactionError"),
    vaultError("vaultError"),
    whitelistingError("whitelistingError"),
    malformedRequestError("malformedRequestError"),
    domainError("domainError"),
    // General catch all
    unexpectedError("unexpectedError");

    private final String text;

    EosioErrorCode(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
