package kpractice.example.com.gbooks.Fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kpractice.example.com.gbooks.DataMangement.DataManager;
import kpractice.example.com.gbooks.Tools.ViewHolder;
import kpractice.example.com.gbooks.R;
import kpractice.example.com.gbooks.Tools.BookItem;
import kpractice.example.com.gbooks.Tools.CommonAdapter;
import kpractice.example.com.gbooks.Tools.CommonListView;
import kpractice.example.com.gbooks.Tools.NewOrderListener;
import pk.wei.com.gserver.Book;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private RecyclerView recommendListView;
    private ArrayList<BookItem> recommendBookList = new ArrayList<>();
    private CommonListView orderedListView;
    private ArrayList<BookItem> orderedBookList = new ArrayList<>();
    private DataManager mDataManager;
    private NewOrderListener mNewOrderListener;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
        
        if (savedInstanceState != null) {
            recommendBookList = savedInstanceState.getParcelableArrayList("recommendBookList");
            orderedBookList = savedInstanceState.getParcelableArrayList("orderedBookList");
        } else {
            ArrayList<Book> booksTmp = mDataManager.getRecommend();
            for (Book book : booksTmp) {
                recommendBookList.add(new BookItem(book.name));
            }
            ArrayList<Book> orderedLisTmp = mDataManager.getOrderedList();
            for (Book book : orderedLisTmp) {
                orderedBookList.add(new BookItem(book.name));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("recommendBookList", recommendBookList);
        outState.putParcelableArrayList("orderedBookList", orderedBookList);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);


        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.tv_title);
        textView.setText(R.string.menu_home_bt_title);

        initRecommendListView();
        initOrderedListView();

        return rootView;
    }

    private void initOrderedListView() {
        orderedListView = (CommonListView) rootView.findViewById(R.id.home_ordered_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        orderedListView.setLayoutManager(linearLayoutManager);
        //添加Android自带的分割线
        orderedListView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        ;
        orderedListView.setEmptyView(R.layout.complex_empty_view);

        CommonAdapter<BookItem> adapter = new CommonAdapter<BookItem>(getContext(), R.layout.book_item, orderedBookList) {
            @Override
            public void convertItemView(ViewHolder holder, BookItem bookItem) {
                TextView bookNameTv = (TextView) holder.getView(R.id.book_item_name);
                bookNameTv.setText(bookItem.getName());
            }
        };
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v.findViewById(R.id.book_item_name);
                String book = tv.getText().toString();
                Log.i(this.getClass().getName(), "onClick: " + book);
                orderedDialog(book);
            }
        });
        orderedListView.setAdapter(adapter);

        mNewOrderListener = new NewOrderListener() {
            @Override
            public void NewBookArrived(Book newBook) {
                ArrayList<Book> orderedLisTmp = mDataManager.getOrderedList();
                orderedBookList.clear();
                for (Book book : orderedLisTmp) {
                    orderedBookList.add(new BookItem(book.name));
                }
                orderedListView.getAdapter().notifyDataSetChanged();
            }
        };
        mDataManager.registerNewOrderListener(mNewOrderListener);
    }

    private void orderedDialog(final String book) {
        AlertDialog alert = null;
        AlertDialog.Builder builder = null;
        builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("你的订阅");
        builder.setMessage("《" + book + "》\n你已经订阅这一本书。 你需要取消订阅吗？");
        builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "好吧", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = "";
                int check = mDataManager.unsubscribeBook(book);
                if (check == 1) {
                    text = "你没有订阅了《" + book + "》.";
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                } else {
                    text = "你已经退订了《" + book + "》";
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNeutralButton("再想想", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "读书一种好习惯哦。", Toast.LENGTH_SHORT).show();
            }
        });
        alert = builder.create(); // 创建AlertDialog对象
        alert.show();              // 显示对话框
    }

    private void initRecommendListView() {

        recommendListView = (RecyclerView) rootView.findViewById(R.id.home_recommend_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recommendListView.setLayoutManager(linearLayoutManager);
        //添加Android自带的分割线
        recommendListView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

//        recommendListView.setEmptyView(R.layout.complex_empty_view);

        CommonAdapter<BookItem> adapter = new CommonAdapter<BookItem>(getContext(), R.layout.book_item, recommendBookList) {
            @Override
            public void convertItemView(ViewHolder holder, BookItem bookItem) {
                TextView bookNameTv = (TextView) holder.getView(R.id.book_item_name);
                bookNameTv.setText(bookItem.getName());
            }
        };
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView bookNameTv = (TextView) v.findViewById(R.id.book_item_name);
                String bookName = bookNameTv.getText().toString();

                recommendDialog(bookName);
            }
        });

        recommendListView.setAdapter(adapter);
    }


    private void recommendDialog(final String book) {
        AlertDialog alert = null;
        AlertDialog.Builder builder = null;
        builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("订阅");
        builder.setMessage("《" + book + "》\n这是一本好书。 你需要订阅吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "好吧", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("订阅", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = "";
                int check = mDataManager.subscribeBook(book);
                if (check == 1) {
                    text = "你成功订阅了《" + book + "》.";
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                } else {
                    text = "你已经订阅了《" + book + "》";
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNeutralButton("再想想", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "欢迎下次订阅哦。", Toast.LENGTH_SHORT).show();
            }
        });
        alert = builder.create(); // 创建AlertDialog对象
        alert.show();              // 显示对话框
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
            textView.setText(R.string.menu_home_bt_title);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDataManager.unRegisterNewOrderListener(mNewOrderListener);
    }
}
