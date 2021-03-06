package com.tttdevs.urlinspector;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView txtUrl;
    Button btnCleanParams, btnOpen;
    String mUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUIElements();

        Intent intent = getIntent();
        String action = intent.getAction();

        if (action.equalsIgnoreCase(Intent.ACTION_SEND) && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String s = intent.getStringExtra(Intent.EXTRA_TEXT);
            try {
                URL u = new URL(s);
                String protocol = u.getProtocol();
                String host = u.getHost();
                String path = u.getPath();
                String query = u.getQuery();

                mUrl = protocol + "://" + host;
                if (path != null)  mUrl += path;

                // TODO: Improve cleaning of utm params
                if (query != null) mUrl += "?"+query.replaceAll("utm_[\\S]+&?", "");;

                txtUrl.setText(mUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        if (action.equalsIgnoreCase(Intent.ACTION_VIEW)) {
            Uri data = intent.getData();
            String host = data.getHost();
            String path = data.getPath();
            String query = data.getQuery();

            mUrl = data.getScheme()+"://"+host;
            if (path != null) mUrl += path;

            // TODO: Improve cleaning of utm params
            if (query != null) mUrl += query.replaceAll("utm_[\\S]+&?", "");;

            txtUrl.setText(mUrl);
        }
    }

    /*
     * Initializes UI elements
     */
    private void initUIElements() {
        txtUrl = (TextView) findViewById(R.id.txtUrl);

        // Erases everything before a ?
        btnCleanParams = (Button) findViewById(R.id.btnCleanParams);
        btnCleanParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUrl = mUrl.split("\\?")[0].split("#")[0];
                txtUrl.setText(mUrl);
            }
        });

        // Opens the displayed link with an external app
        btnOpen = (Button) findViewById(R.id.btnOpen);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Validate URL format
                if (!mUrl.equals("")) {
                    Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse(String.valueOf(txtUrl.getText())));
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, R.string.empty_string, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
