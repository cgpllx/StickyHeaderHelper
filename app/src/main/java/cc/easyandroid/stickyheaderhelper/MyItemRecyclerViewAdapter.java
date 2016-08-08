package cc.easyandroid.stickyheaderhelper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cc.easyandroid.easyrecyclerview.EasyRecyclerAdapter;
import cc.easyandroid.stickyheaderhelper.dummy.DummyContent.DummyItem;


public class MyItemRecyclerViewAdapter extends EasyRecyclerAdapter<DummyItem> implements StickAdapter{


    public MyItemRecyclerViewAdapter(List<DummyItem> items) {
        addDatas(items);
    }


    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int position, DummyItem dummyItem) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.mItem = dummyItem;
            holder.mIdView.setText(dummyItem.id);
            holder.mContentView.setText(dummyItem.content);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateStickViewHolder() {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
