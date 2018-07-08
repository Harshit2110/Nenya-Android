package in.evolve.angelhack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button loginButton;
    private Button signUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        loginButton= (Button) findViewById(R.id.login_button);
        signUpButton= (Button) findViewById(R.id.signup_button);
        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int id=view.getId();

        switch (id)
        {
            case R.id.signup_button:
                Intent intent=new Intent(MainActivity.this,MyAccountFormActivity.class);
                startActivity(intent);
                break;

            case R.id.login_button:
                Intent intent1=new Intent(MainActivity.this,UserProfileActivity.class);
                startActivity(intent1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
}
