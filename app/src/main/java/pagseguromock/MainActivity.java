package pagseguromock;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import objective.pagseguromock.R;


public class MainActivity extends Activity {

    public static final String ENCODING = "UTF-8";

    private EditText errorCodeEdit;
    private EditText errorEdit;
    private EditText paymentIdEdit;
    private EditText paymentStatusEdit;
    private EditText referenceEdit;
    private EditText returnUrlEdit;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, String> parameterMap = parseParameters();

        errorCodeEdit = (EditText) findViewById(R.id.errorCodeEdit);
        errorCodeEdit.setText("1001");

        errorEdit = (EditText) findViewById(R.id.errorEdit);
        errorEdit.setText("Valor para cobrança abaixo do valor mínimo (R$ 1,00)");

        paymentIdEdit = (EditText) findViewById(R.id.paymentIdEdit);
        paymentIdEdit.setText("EB636339-F853-4E39-B627-573395EC2BE9");

        paymentStatusEdit = (EditText) findViewById(R.id.paymentStatusEdit);
        paymentStatusEdit.setText("1");

        referenceEdit = (EditText) findViewById(R.id.referenceEdit);
        referenceEdit.setText(parameterMap.get("reference"));

        returnUrlEdit = (EditText) findViewById(R.id.urlRetornoEdit);
        returnUrlEdit.setText(parameterMap.get("returnUrl"));

        backButton = (Button) findViewById(R.id.voltar);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(buildUrl()));
                startActivity(browserIntent);
            }

        });
    }

    private String buildUrl() {
        String url = returnUrlEdit.getText() + "?";
        url += "errorCode=" + encodeUrlText(errorCodeEdit);
        url += "&error=" + encodeUrlText(errorEdit);
        url += "&paymentId=" + encodeUrlText(paymentIdEdit);
        url += "&paymentStatus=" + encodeUrlText(paymentStatusEdit);
        url += "&reference=" + encodeUrlText(referenceEdit);
        return url;
    }

    private String encodeUrlText(EditText editText) {
        try {
            return URLEncoder.encode(editText.getText().toString(), ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private Map<String, String> parseParameters() {
        Map<String, String> parameterMap = new HashMap<String, String>();

        String data = getIntent().getDataString();
        if (data != null) {
            String params = data.substring(data.indexOf("?"));
            displayParams(params);
            String[] paramsKeyValue = params.split("&");
            for (String paramKeyValue : paramsKeyValue) {
                String[] keyValue = paramKeyValue.split("=");
                if (keyValue.length > 1)
                    parameterMap.put(keyValue[0], keyValue[1]);
            }

        }

        return parameterMap;
    }

    private void displayParams(String params) {
        TextView txt = (TextView) findViewById(R.id.hello);
        txt.setText("Parâmetros recebidos: " + params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }
}
