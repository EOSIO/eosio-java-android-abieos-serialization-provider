package one.block.eosiojavaabieosserializationprovider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class EosioError extends Exception {

    public @NonNull EosioErrorCode errorCode = EosioErrorCode.unexpectedError;
    public @NonNull String reason = "";
    public @NonNull String context = "";
    public @Nullable Error originalError = null;
    public boolean isReturnable = true;

    public EosioError(@NonNull EosioErrorCode errorCode, @NonNull String reason) {
        this(errorCode, reason, "", null, true);
    }

    public EosioError(@NonNull EosioErrorCode errorCode, @NonNull String reason, @NonNull String context, boolean isReturnable) {
        this(errorCode, reason, context, null, isReturnable);
    }

    public EosioError(@NonNull EosioErrorCode errorCode,
                      @NonNull String reason,
                      @NonNull String context,
                      @Nullable Error originalError,
                      boolean isReturnable) {
        super(reason, originalError);
        this.errorCode = errorCode;
        this.reason = reason;
        this.context = context;
        this.originalError = originalError;
        this.isReturnable = isReturnable;
    }

    @NonNull
    public String errorDescription() {
        if(this.context.isEmpty()) {
            return String.format("%s: %s", this.errorCode.toString(), this.reason);
        } else {
            return String.format("%s: %s: context: %s", this.errorCode.toString(), reason, context);
        }
    }

    @NonNull
    public String description() {
        return this.getLocalizedMessage();
    }

    @NonNull
    public String asJsonString() {
        try {
            JSONObject errInfo = new JSONObject();
            errInfo.put("errorCode", this.errorCode.toString());
            errInfo.put("reason", this.reason);
            errInfo.put("contextualInfo", this.context);
            JSONObject err = new JSONObject();
            err.put("errorType", "EosioError");
            err.put("errorInfo", errInfo);
            return err.toString(4);
        } catch (JSONException ex) {
            return "{}";
        }
    }

    @NonNull
    public static EosioError asEosioError(@NonNull Exception ex) {
        return (ex instanceof EosioError) ? (EosioError) ex : new EosioError(EosioErrorCode.unexpectedError, ex.getLocalizedMessage());
    }
}
