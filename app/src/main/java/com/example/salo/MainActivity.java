package com.example.salo;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.salo.prductview.ProductGridFragment;


public class MainActivity extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            String token = getToken();
            if (token != null && !token.isEmpty()) {
                this.currentFragment = new ProductGridFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, this.currentFragment)
                        .commit();
            } else {
                this.currentFragment = new LoginFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, this.currentFragment)
                        .commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.home:
                this.navigateTo(new ProductGridFragment(), true);
                return true;
            case R.id.login:
                this.navigateTo(new LoginFragment(), true);
                return true;
            case R.id.register:
                this.navigateTo(new RegisterFragment(), true);
                return true;
            case R.id.logout:
                this.removeToken();
                this.navigateTo(new LoginFragment(), true);
                return true;
            case R.id.item3:
                Toast.makeText(this, "Item 3 selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.subitem1:
                Toast.makeText(this, "Sub Item 1 selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.subitem2:
                Toast.makeText(this, "Sub Item 2 selected", Toast.LENGTH_LONG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
