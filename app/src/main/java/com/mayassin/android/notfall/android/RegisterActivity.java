package com.mayassin.android.notfall.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mayassin.android.notfall.R;

/**
 * Created by mohamed on 1/28/17.
 */

public class RegisterActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Button nextPageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewPager = (ViewPager) findViewById(R.id.pager);

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        viewPager.beginFakeDrag();

        nextPageButton = (Button) findViewById(R.id.registerButton);

        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNextPage();
            }
        });


    }

    private void goToNextPage() {
        int currentPage = viewPager.getCurrentItem();
        switch (currentPage) {
            case 0:
                // SET ALL THE VALUES TO APPROPRIATE STRING
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
        viewPager.setCurrentItem(currentPage + 1);
        init(currentPage+1);


    }

    private void init(int fragmentNumber) {
        switch (fragmentNumber) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
    }


    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            RegisterFragment frag = new RegisterFragment();
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    bundle.putInt("page", 1);
                    frag.setArguments(bundle);
                    return frag;
                case 1:
                    bundle.putInt("page", 2);
                    frag.setArguments(bundle);
                    return frag;
                case 2:
                    bundle.putInt("page", 3);
                    frag.setArguments(bundle);
                    return frag;
                default:
                    bundle.putInt("page", 1);
                    frag.setArguments(bundle);
                    return frag;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "FirstNameLastNameCompanyPhone";
                case 1:
                    return "EmailPasswordPassword";
                case 2:
                    return "PhotoUpload";
                default:
                    return "FirstNameLastNameCompanyPhone";
            }
        }
    }
}
