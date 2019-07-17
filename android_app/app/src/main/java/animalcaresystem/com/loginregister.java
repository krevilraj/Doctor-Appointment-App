package animalcaresystem.com;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class loginregister extends AppCompatActivity {
ViewPager viewPager;
TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginregister);

        viewPager=findViewById(R.id.viewpager);
        tabLayout=findViewById(R.id.tablayout);

        FragmentAdpater fragmentAdpater= new FragmentAdpater(getSupportFragmentManager());
       fragmentAdpater.addFragment(new loginfragment(),"login");
        fragmentAdpater.addFragment(new signupfragment(),"signup");

        viewPager.setAdapter(fragmentAdpater);
        tabLayout.setupWithViewPager(viewPager);

    }
}
