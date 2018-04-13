package kpractice.example.com.gbooks.Tools;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CommonListView extends RecyclerView {


    private View emptyView;
    private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            // 设置空 view 原理都一样，没有数据显示空view，有数据隐藏空view
            CommonAdapter adapter = (CommonAdapter)getAdapter();
            if (emptyView != null)
            if (adapter.getItemCount() == 0) {
                emptyView.setVisibility(VISIBLE);
                CommonListView.this.setVisibility(INVISIBLE);
            } else {
                emptyView.setVisibility(INVISIBLE);
                CommonListView.this.setVisibility(VISIBLE);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            onChanged();
        }
    };

    public CommonListView(Context context) {
        super(context);
    }

    public CommonListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setEmptyView(int layoutId) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        emptyView = inflater.inflate(layoutId, (ViewGroup) this.getParent(), false);
//
        ((ViewGroup) this.getParent()).addView(emptyView);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(observer);
        observer.onChanged();
    }
}
