package kpractice.example.com.gbooks.Fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kpractice.example.com.gbooks.DataMangement.DataManager;
import kpractice.example.com.gbooks.R;
import kpractice.example.com.gbooks.Tools.BookItem;
import kpractice.example.com.gbooks.Tools.CommonAdapter;
import kpractice.example.com.gbooks.Tools.CommonListView;
import kpractice.example.com.gbooks.Tools.NewOrderListener;
import kpractice.example.com.gbooks.Tools.ViewHolder;
import pk.wei.com.gserver.Book;

public class OrderedFragment extends Fragment {

    private View rootView;
    private DataManager mDataManager;
    private CommonListView orderedListView;
    private ArrayList<BookItem> orderedBookList = new ArrayList<>();
    private NewOrderListener mNewOrderListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mDataManager = DataManager.getInstance();

        if (savedInstanceState != null) {
            orderedBookList = savedInstanceState.getParcelableArrayList("orderedBookList");
        } else {
            ArrayList<Book> orderedLisTmp = mDataManager.getOrderedList();
            for (Book book : orderedLisTmp) {
                orderedBookList.add(new BookItem(book.name));
            }
        }

        rootView = inflater.inflate(R.layout.fragment_ordered, container, false);

        initOrderedListView();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("orderedBookList", orderedBookList);
        super.onSaveInstanceState(outState);
    }

    private void initOrderedListView() {
        orderedListView = (CommonListView) rootView.findViewById(R.id.ordered_list);
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
                int check = mDataManager.unSubscribeBook(book);
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        mDataManager.unRegisterNewOrderListener(mNewOrderListener);
    }
}
