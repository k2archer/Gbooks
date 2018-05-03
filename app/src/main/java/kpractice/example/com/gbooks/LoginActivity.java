package kpractice.example.com.gbooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import kpractice.example.com.gbooks.DataMangement.DataManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DataManager.getInstance();
        initView();
    }

    private void initView() {
        findViewById(R.id.login_btn).setOnClickListener(this);
        name = (EditText) findViewById(R.id.name_etx);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                int check = DataManager.getInstance().login(name.getText().toString());
                if (check == 1) {
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    this.finish();
                } else {
                    Toast.makeText(this, "Login fail." + check, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
