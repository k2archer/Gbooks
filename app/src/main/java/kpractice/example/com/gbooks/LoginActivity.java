package kpractice.example.com.gbooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.menu_item_setting);
        menu.add(0, 2, 1, R.string.menu_item_about);
        menu.add(0, 3, 1, R.string.menu_item_exit);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            case 2:
                i = new Intent(this, AboutActivity.class);
                startActivity(i);
                break;
            case 3:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
