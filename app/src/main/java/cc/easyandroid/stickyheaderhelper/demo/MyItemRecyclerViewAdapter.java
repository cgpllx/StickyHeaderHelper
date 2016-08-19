package cc.easyandroid.stickyheaderhelper.demo;

import android.app.Activity;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cc.easyandroid.easyrecyclerview.EasyRecyclerAdapter;
import cc.easyandroid.stickyheaderhelper.demo1.dummy.DummyContent;
import cc.easyandroid.stickyrecyclerheader.StickyHeaderHelper;
import cc.easyandroid.stickyrecyclerheader.StickyRecyclerHeadersAdapter;


public class MyItemRecyclerViewAdapter extends EasyRecyclerAdapter<DummyContent.DummyItem> implements StickyRecyclerHeadersAdapter {

    public static final int STICKYHEADERTYPE = 0x11;
    public static final int DEFAULTTYPE = 0x10;

    public MyItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
        addDatas(items);
        registerAdapterDataObserver(new AdapterDataObserver());
    }


    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case STICKYHEADERTYPE:
                return onCreateHeaderViewHolder(viewGroup, viewType);
            case DEFAULTTYPE:
            default:
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_item, viewGroup, false);
                return new ViewHolder(view);
        }
    }


    @Override
    public int onCreatItemViewType(int position) {
        DummyContent.DummyItem dummyItem = getData(position + getHeaderCount());
        if (dummyItem != null && dummyItem.stickyheader) {
            return STICKYHEADERTYPE;
        }
        return DEFAULTTYPE;
    }


    /**
     * Retrieve the global position of the Item in the Adapter list.
     *
     * @return the global position in the Adapter if found, -1 otherwise
     * @since 5.0.0-b1
     */
    public void onBind(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.mItem = getData(position);
            if (holder.mIdView != null && holder.mContentView != null) {
                holder.mIdView.setText(holder.mItem.id);
                holder.mContentView.setText(holder.mItem.content);
            }
        } else if (viewHolder instanceof MyStickViewHolder) {
            MyStickViewHolder stickViewHolder = (MyStickViewHolder) viewHolder;
            stickViewHolder.content.setText("position=" + position + getData(position).details);
            stickViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "imageView id=" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class MyStickViewHolder extends StickViewHolder {
        ImageView imageView;
        TextView content;

        public MyStickViewHolder(View item, StickyRecyclerHeadersAdapter adapter) {
            super(item, adapter);
            imageView = (ImageView) item.findViewById(R.id.imageView);
            content = (TextView) item.findViewById(R.id.content);
        }
    }


    public ViewGroup getStickySectionHeadersHolder() {
        return (ViewGroup) ((Activity) mRecyclerView.getContext()).findViewById(R.id.sticky_header_container);
    }

    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_stick, viewGroup, false);
        return new MyStickViewHolder(view, this);
    }


    public int getGlobalPositionOf(@NonNull Object item) {
        return item != null && mDatas != null && !mDatas.isEmpty() ? mDatas.indexOf(item) + getHeaderCount() : -1;
    }

    public DummyContent.DummyItem getSectionHeader(@IntRange(from = 0) int position) {
        //Headers are not visible nor sticky
        //When headers are visible and sticky, get the previous header
        for (int i = position; i >= 0; i--) {
            DummyContent.DummyItem item = getData(i);
            if (item != null && item.stickyheader) return item;
        }
        return null;
    }

    StickyHeaderHelper stickyHeaderHelper;

    RecyclerView mRecyclerView;

    private class AdapterDataObserver extends RecyclerView.AdapterDataObserver {


        private void updateOrClearHeader() {
            if (stickyHeaderHelper != null) {
                stickyHeaderHelper.updateOrClearHeader(true);
            }
        }

        /* Triggered by notifyDataSetChanged() */
        @Override
        public void onChanged() {
            updateOrClearHeader();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            updateOrClearHeader();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            updateOrClearHeader();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            updateOrClearHeader();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            updateOrClearHeader();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        if (stickyHeaderHelper != null) {
            stickyHeaderHelper.attachToRecyclerView(mRecyclerView);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (stickyHeaderHelper != null) {
            stickyHeaderHelper.detachFromRecyclerView(mRecyclerView);
            stickyHeaderHelper = null;
        }
        super.onDetachedFromRecyclerView(recyclerView);
    }

    private OnStickyHeaderChangeListener mStickyHeaderChangeListener;

    public void setmStickyHeaderChangeListener(OnStickyHeaderChangeListener stickyHeaderChangeListener) {
        mStickyHeaderChangeListener = stickyHeaderChangeListener;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public MyItemRecyclerViewAdapter setStickyHeaders(boolean headersSticky) {
        // Add or Remove the sticky headers
        if (headersSticky) {
            if (stickyHeaderHelper == null) {
                stickyHeaderHelper = new StickyHeaderHelper(this, mStickyHeaderChangeListener);
            }
            if (!stickyHeaderHelper.isAttachedToRecyclerView()) {
                stickyHeaderHelper.attachToRecyclerView(mRecyclerView);
            }
        } else if (stickyHeaderHelper != null) {
            stickyHeaderHelper.detachFromRecyclerView(mRecyclerView);
            stickyHeaderHelper = null;
        }
        return this;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyContent.DummyItem mItem;

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
