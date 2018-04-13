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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kpractice.example.com.gbooks.DataMangement.DataManager;
import kpractice.example.com.gbooks.Tools.BookItem;
import kpractice.example.com.gbooks.Tools.CommonAdapter;
import kpractice.example.com.gbooks.Tools.ViewHolder;
import kpractice.example.com.gbooks.R;
import kpractice.example.com.gbooks.Tools.CommonListView;
import pk.wei.com.gserver.Book;

public class BorrowFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private DataManager mDataManager;
    private EditText searchWord;
    private CommonListView searchedListView;
    ArrayList<BookItem> foundBookList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_borrow, container, false);

        searchedListView = (CommonListView) rootView.findViewById(R.id.searched_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        searchedListView.setLayoutManager(linearLayoutManager);
        searchedListView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        searchedListView.setEmptyView(R.layout.complex_empty_view);

        CommonAdapter<BookItem> adapter = new CommonAdapter<BookItem>(getContext(), R.layout.book_item, foundBookList) {
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
                orderedDialog(bookName);
            }
        });

        searchedListView.setAdapter(adapter);

        initView();

        return rootView;
    }

    private void orderedDialog(final String book) {
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

    private void initView() {
        rootView.findViewById(R.id.search_image_btn).setOnClickListener(this);

        searchWord = (EditText) rootView.findViewById(R.id.search_word_dt);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataManager = DataManager.getInstance();

        if (savedInstanceState != null) {
            foundBookList = savedInstanceState.getParcelableArrayList("foundBookList");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("foundBookList", foundBookList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {  //不在最前端界面显示
        } else {  //重新显示到最前端
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.tv_title);
            textView.setText(R.string.menu_borrow_bt_title);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_image_btn:
                String text = searchWord.getText().toString();
                if (!text.isEmpty()) {
                    List<Book> books = DataManager.getInstance().findBook(text);
                    foundBookList.clear();
                    for (Book book : books) {
                        foundBookList.add(new BookItem(book.name));
                    }
                    searchedListView.getAdapter().notifyDataSetChanged();
                }

                break;
        }
    }
}
