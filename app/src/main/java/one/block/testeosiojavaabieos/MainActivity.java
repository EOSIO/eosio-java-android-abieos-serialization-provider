package one.block.testeosiojavaabieos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import one.block.eosiojava.error.serializationprovider.SerializationProviderError;
import one.block.eosiojavaabieosserializationprovider.AbiEos;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        String results = "Waiting for AbiEos";
        try {
            AbiEos abiEos = new AbiEos();
            String contractStrOrig = "eosio.assert";
            long name64 = abiEos.stringToName64(contractStrOrig);
            String testStr = abiEos.name64ToString(name64);

            results = String
                    .format("Original: %s\nname64: %d\ntest: %s", contractStrOrig, name64, testStr);
            tv.setText(results);
        } catch (SerializationProviderError serializationProviderError) {
            results = "Error from SerializationProvider: " + serializationProviderError.getLocalizedMessage();
        }
        tv.setText(results);
    }

}
