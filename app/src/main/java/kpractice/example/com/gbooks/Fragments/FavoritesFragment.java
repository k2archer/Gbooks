package kpractice.example.com.gbooks.Fragments;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import kpractice.example.com.gbooks.R;

public class FavoritesFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.tv_title);
        textView.setText(R.string.menu_me_favorite);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.tv_title);
        textView.setText(R.string.menu_me_bt_title);
    }
}
