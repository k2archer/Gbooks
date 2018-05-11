package kpractice.example.com.gbooks.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kpractice.example.com.gbooks.Tools.ViewHolder;
import kpractice.example.com.gbooks.DataMangement.DataManager;
import kpractice.example.com.gbooks.R;
import kpractice.example.com.gbooks.Tools.CommonAdapter;
import kpractice.example.com.gbooks.Tools.CommonListView;
import kpractice.example.com.gbooks.Tools.MessageItem;

public class MessageFragment extends Fragment {

    private View rootView;
    private DataManager mDataManager;
    private CommonListView messageListView;
    private ArrayList<MessageItem> mMessageList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();

        if (savedInstanceState != null) {
            mMessageList = savedInstanceState.getParcelableArrayList("mMessageList");
        } else {
            mMessageList = new ArrayList<>();

            ArrayList<String> list = mDataManager.getMessagesList();
            for (String item: list) {
                mMessageList.add(new MessageItem(item));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_message, container, false);

        init();
        initMessageList();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("mMessageList", mMessageList);
        super.onSaveInstanceState(outState);
    }

    private void init() {

    }

    private void initMessageList() {
        messageListView = (CommonListView) rootView.findViewById(R.id.message_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        messageListView.setLayoutManager(linearLayoutManager);
        //添加Android自带的分割线
        messageListView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        messageListView.setEmptyView(R.layout.complex_empty_view);
        CommonAdapter<MessageItem> adapter = new CommonAdapter<MessageItem>(getContext(), R.layout.message_item, mMessageList) {
            @Override
            public void convertItemView(ViewHolder holder, MessageItem item) {
                TextView textTv = (TextView) holder.getView(R.id.message_item_text);
                textTv.setText(item.mText);
            }
        };
        messageListView.setAdapter(adapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // 不在最前端界面显示
        } else {
            // 重新显示到最前端
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.tv_title);
            textView.setText(R.string.menu_message_bt_title);
        }
    }
}