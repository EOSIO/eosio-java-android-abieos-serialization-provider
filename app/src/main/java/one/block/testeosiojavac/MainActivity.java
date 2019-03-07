package one.block.testeosiojavac;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import one.block.eosiojavaabieosserializationprovider.AbiEos;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        AbiEos abiEos = new AbiEos();
        String contractStrOrig = "eosio.assert";
        long name64 = abiEos.stringToName64(contractStrOrig);
        String testStr = abiEos.name64ToString(name64);

        String results = String.format("Original: %s\nname64: %d\ntest: %s", contractStrOrig, name64, testStr);
        tv.setText(results);
    }

}
