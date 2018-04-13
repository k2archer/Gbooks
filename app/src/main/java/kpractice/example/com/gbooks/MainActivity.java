package kpractice.example.com.gbooks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import kpractice.example.com.gbooks.Fragments.HomeFragment;
import kpractice.example.com.gbooks.Fragments.MessageFragment;
import kpractice.example.com.gbooks.Fragments.UserCenterFragment;
import kpractice.example.com.gbooks.Fragments.BorrowFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    public class Item implements Serializable {
//        public RelativeLayout layout;
//        public ImageView image;
//        public TextView text;
//        public Fragment fragment;
//
//        public Item(int layoutId, int imageViewId, int textViewId) {
//            this.layout = (RelativeLayout) findViewById(layoutId);
//            this.image = (ImageView) findViewById(imageViewId);
//            this.text = (TextView) findViewById(textViewId);
//        }
//    }


//    class Resource {
//        public int layoutId;
//        public int imageViewId;
//        public int textViewId;
//
//        Resource(int layout, int image, int text) {
//            layoutId = layout;
//            imageViewId = image;
//            textViewId = text;
//        }
//    }

//    Resource[] itemId = {
//            new Resource(R.id.layout_home, R.id.img_home, R.id.tv_home),
//            new Resource(R.id.layout_borrow, R.id.img_borrow, R.id.tv_borrow),
//            new Resource(R.id.layout_message, R.id.img_message, R.id.tv_message),
//            new Resource(R.id.layout_usercenter, R.id.img_usercenter, R.id.tv_usercenter),
//    };

    //    private HashMap<Integer, Fragment> itemMap = new HashMap<>();
    private HashMap<Integer, Class<?>> itemMap = new HashMap<>();
    private int currentIndex = 0;
    Integer[] menuId = {
            R.id.menu_home_bt,
            R.id.menu_borrow_bt,
            R.id.menu_message_bt,
            R.id.menu_me_bt
    };
    ArrayList<Integer> menu = new ArrayList<>(Arrays.asList(menuId));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.action_bar_top_base);

        setContentView(R.layout.activity_main);

        initItemEvent();

        if (savedInstanceState != null) {
            RestoreInstanceSate(savedInstanceState);
        } else {
            init();
        }
    }

    private void RestoreInstanceSate(Bundle savedInstanceState) {
        currentIndex = savedInstanceState.getInt("currentIndex");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("currentIndex", currentIndex);

        super.onSaveInstanceState(savedInstanceState);
    }

    public void init() {
        initItemEvent();
        initFragmentPaper();
    }

    public void initItemEvent() {
        for (int it : menuId) {
            findViewById(it).setOnClickListener(this);
        }
    }

    public void initFragmentPaper() {
        itemMap.put(R.id.menu_home_bt, HomeFragment.class);
        itemMap.put(R.id.menu_borrow_bt, BorrowFragment.class);
        itemMap.put(R.id.menu_message_bt, MessageFragment.class);
        itemMap.put(R.id.menu_me_bt, UserCenterFragment.class);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        try {
            currentIndex = menu.get(0);
            Class<?> cl = itemMap.get(currentIndex);
            Fragment fragment = (Fragment) cl.newInstance();
            transaction.add(R.id.frame_content, fragment, String.valueOf(currentIndex));
            transaction.show(fragment);
            transaction.commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        int index = view.getId();
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(String.valueOf(index));
        if (fragment != null) {
            if (currentIndex != index) {
                Fragment currentFragment = manager.findFragmentByTag(String.valueOf(currentIndex));
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.hide(currentFragment);
                transaction.show(fragment).commit();
            }
            currentIndex = index;
        } else if (menu.contains(index)) {
            try {
                Fragment currentFragment = manager.findFragmentByTag(String.valueOf(currentIndex));
                FragmentTransaction transaction = manager.beginTransaction();
                Class<?> cl = itemMap.get(index);
                Fragment newFragment = (Fragment) cl.newInstance();
                if (!newFragment.isAdded()) {
                    transaction.add(R.id.frame_content, newFragment, String.valueOf(index));
                }
                transaction.hide(currentFragment);
                transaction.show(newFragment).commit();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            currentIndex = index;
        }

    }

}
