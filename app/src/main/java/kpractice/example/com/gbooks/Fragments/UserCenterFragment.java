package kpractice.example.com.gbooks.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import kpractice.example.com.gbooks.AboutActivity;
import kpractice.example.com.gbooks.DataMangement.DataManager;
import kpractice.example.com.gbooks.R;

public class UserCenterFragment extends Fragment {

    private View rootView;
    private DataManager mDataManager;

    public UserCenterFragment() {
        mDataManager = DataManager.getInstance();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_center, container, false);

        init();
        return rootView;

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // 不在最前端界面显示
        } else {
            // 重新显示到最前端
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.tv_title);
            textView.setText(R.string.menu_me_bt_title);
        }
    }

    private void init() {
        TextView userName = (TextView) rootView.findViewById(R.id.user_name_tv);
        userName.setText(mDataManager.getUserName());
        // TODO: 加载更多的用户信息
        ImageView userIcon = (ImageView) rootView.findViewById(R.id.user_icon);
//        userIcon.setImageIcon(mDataManager.getUserIcon());
        TextView userId = (TextView) rootView.findViewById(R.id.user_id_tv);
//        userId.setText(mDataManager.getUserId());

        TextView aboutTv = (TextView) rootView.findViewById(R.id.about_tv);
        aboutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AboutActivity.class);
                startActivity(i);
            }
        });

        final FavoritesFragment favoritesFragment = new FavoritesFragment();
        TextView favoriteTv = (TextView) rootView.findViewById(R.id.favorite_tv);
        favoriteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.frame_content, favoritesFragment, String.valueOf(R.layout.fragment_favorites));
                transaction.show(favoritesFragment);
                transaction.commit();
            }
        });

    }
}
