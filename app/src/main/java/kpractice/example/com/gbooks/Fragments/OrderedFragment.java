package kpractice.example.com.gbooks.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kpractice.example.com.gbooks.R;

public class OrderedFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ordered, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.tv_title);
        textView.setText(R.string.menu_me_ordered);

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
