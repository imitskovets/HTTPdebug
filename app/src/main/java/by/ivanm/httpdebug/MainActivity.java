package by.ivanm.httpdebug;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


public class MainActivity extends Activity {

    String method = "POST";
    String requestUri = "http://";
    String requestBody = "";
    String responseBody = "main";
    DialogFragment dialog1;

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog1 = new Dialog1();

        final EditText editTextServer =  (EditText)findViewById(R.id.editTextServer);
        final EditText editTextRequestBody =  (EditText)findViewById(R.id.editTextRequestBody);

        RadioGroup radiogroup = (RadioGroup) findViewById(R.id.radioGroup);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        Toast.makeText(getApplicationContext(), "No choice", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonGet:
                        method = "GET";
                        Toast.makeText(getApplicationContext(), "Get", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonPost:
                        method = "POST";
                        Toast.makeText(getApplicationContext(), "Post", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

        Button buttonGo = (Button) findViewById(R.id.buttonGo);
        buttonGo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultViewer.class);
                MyFilesStr myFilesStr = new MyFilesStr();
                //myFilesStr.writeToFile(getApplicationContext(), "file.txt", "WTF");
                responseBody =  myFilesStr.readFile(getApplicationContext(),"file.txt");
                intent.putExtra("respBody",responseBody);
                startActivity(intent);
            }
        });

        Button buttonDialog = (Button) findViewById(R.id.buttonDialog);
        buttonDialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog1.show(getFragmentManager(), "dialog 1");
            }
        });

        Button buttonDo = (Button) findViewById(R.id.buttonDownload);
        buttonDo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                requestUri = "http://" + editTextServer.getText().toString() + "/";
                requestBody = editTextRequestBody.getText().toString();
                //TODO then the fields are empty
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                //TODO use only wifi?
                if (networkInfo != null && networkInfo.isConnected()) {
                    ServCon task = new ServCon();
                    task.method = method;
                    task.requestBody = requestBody;
                    task.destination = String.valueOf(getApplicationContext().getFilesDir()) + "/file.txt";
                    task.execute(requestUri);
                } else {
                    Toast.makeText(getApplicationContext(), "No network connection available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
