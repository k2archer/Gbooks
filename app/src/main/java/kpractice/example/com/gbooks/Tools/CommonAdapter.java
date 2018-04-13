package kpractice.example.com.gbooks.Tools;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private View.OnClickListener onClickListener;

    private Context mContext;
    private int mLayoutId;
    private List<T> mData;


    public CommonAdapter(Context context, int layoutId, List<T> data) {
        mContext = context;
        mLayoutId = layoutId;
        mData = data;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.onClickListener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ViewHolder holder = ViewHolder.get(mContext, parent, mLayoutId);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setOnClickListener(onClickListener);
        convertItemView(holder, mData.get(position));
    }

    public abstract void convertItemView(ViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
