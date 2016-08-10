package cc.easyandroid.stickyheaderhelper;

import android.app.Activity;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cc.easyandroid.easyrecyclerview.EasyRecyclerAdapter;
import cc.easyandroid.stickyheaderhelper.dummy.DummyContent.DummyItem;


public class MyItemRecyclerViewAdapter extends EasyRecyclerAdapter<DummyItem> implements StickyRecyclerHeadersAdapter, StickyRecyclerHeadersAdapter.OnStickyHeaderChangeListener {

    public static final int STICKYHEADERTYPE = 0x11;
    public static final int DEFAULTTYPE = 0x10;

    public MyItemRecyclerViewAdapter(List<DummyItem> items) {
        addDatas(items);
//        stickyHeaderHelper=new StickyHeaderHelper(this,this);
        registerAdapterDataObserver(new AdapterDataObserver());
//        stickyHeaderHelper.attachToRecyclerView(mRecyclerView);
    }


    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup viewGroup, int viewType) {
//        viewGroup.clic
        switch (viewType) {
            case STICKYHEADERTYPE:
                return onCreateHeaderViewHolder(viewGroup, viewType);
            case DEFAULTTYPE:
            default:
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_item, viewGroup, false);
                return new ViewHolder(view);
        }
    }


    public DummyItem getData(int position) {
        int realPosition = position - mHeaderViews.size();
        if (realPosition >= 0 && realPosition < getNormalItemCount()) {
            return mDatas.get(realPosition);
        }
        return null;
    }

    @Override
    public int onCreatItemViewType(int position) {

        DummyItem dummyItem = getData(position + getHeaderCount());
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
//    public int getGlobalPositionOf(@NonNull IFlexible item) {
//        return item != null && mItems != null && !mItems.isEmpty() ? mItems.indexOf(item) : -1;
//    }
//    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.mItem = getData(position);
            if (holder.mIdView != null && holder.mContentView != null) {
                holder.mIdView.setText(holder.mItem.id);
                holder.mContentView.setText(holder.mItem.content);
            }
        } else if (viewHolder instanceof StickViewHolder) {
            StickViewHolder stickViewHolder = (StickViewHolder) viewHolder;
            stickViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "imageView id=" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public long getHeaderId(int position) {

        return 0;
    }

    public ViewGroup getStickySectionHeadersHolder() {
        return (ViewGroup) ((Activity) mRecyclerView.getContext()).findViewById(R.id.sticky_header_container);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_stick, viewGroup, false);
        return new StickViewHolder(view, this);
    }

    /**
     * Retrieve the global position of the Item in the Adapter list.
     *
     * @param item the item to find
     * @return the global position in the Adapter if found, -1 otherwise
     * @since 5.0.0-b1
     */
    public int getGlobalPositionOf(@NonNull DummyItem item) {
        return item != null && mDatas != null && !mDatas.isEmpty() ? mDatas.indexOf(item)+getHeaderCount()   : -1;
    }

    public DummyItem getSectionHeader(@IntRange(from = 0) int position) {
        //Headers are not visible nor sticky
        //When headers are visible and sticky, get the previous header
        for (int i = position; i >= 0; i--) {
            DummyItem item = getData(i);
            if (item != null && item.stickyheader) return item;
        }
        return null;
    }

    StickyHeaderHelper stickyHeaderHelper;

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

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

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public MyItemRecyclerViewAdapter setStickyHeaders(boolean headersSticky) {
        // Add or Remove the sticky headers
        if (headersSticky) {
            if (stickyHeaderHelper == null)
                stickyHeaderHelper = new StickyHeaderHelper(this, this);
            if (!stickyHeaderHelper.isAttachedToRecyclerView())
                stickyHeaderHelper.attachToRecyclerView(mRecyclerView);
        } else if (stickyHeaderHelper != null) {
//            this.headersSticky = false;
//            mStickyHeaderHelper.detachFromRecyclerView(mRecyclerView);
//            mStickyHeaderHelper = null;
        }
        return this;
    }

    @Override
    public void onStickyHeaderChange(int sectionIndex) {

    }

    public class StickViewHolder extends RecyclerView.ViewHolder {
        private View contentView;
        private int mBackupPosition = RecyclerView.NO_POSITION;
        private ImageView imageView;

        public StickViewHolder(View item, final MyItemRecyclerViewAdapter adapter) {
            super(new FrameLayout(item.getContext()));
            itemView.setLayoutParams(adapter.getRecyclerView().getLayoutManager()
                    .generateLayoutParams(item.getLayoutParams()));
            ((FrameLayout) itemView).addView(item);//Add View after setLayoutParams
            contentView = itemView;
            imageView = (ImageView) item.findViewById(R.id.imageView);

//
//            getContentView().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    adapter.performItemClick(v, getAdapterPosition());
//                }
//            });
        }

        public View getContentView() {
            return contentView != null ? contentView : itemView;
        }

        /**
         * Overcomes the situation of returning an unknown position (-1) of ViewHolders created out of
         * the LayoutManager (ex. StickyHeaders).
         * <p><b>NOTE:</b> Always call this method, instead of {@code getAdapterPosition()}, in case
         * of StickyHeaders use case.</p>
         *
         * @return the Adapter position result of {@link #getAdapterPosition()} OR the backup position
         * preset and known, if the previous result was {@link RecyclerView#NO_POSITION}.
         * @see #setBackupPosition(int)
         * @since 5.0.0-b6
         */
        public int getFlexibleAdapterPosition() {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                position = mBackupPosition;
            }
            return position;
        }

        /**
         * Restore the Adapter position if the original Adapter position is unknown.
         * <p>Called by StickyHeaderHelper to support the clickListeners events.</p>
         *
         * @param backupPosition the known position of this ViewHolder
         * @since 5.0.0-b6
         */
        public void setBackupPosition(int backupPosition) {
            mBackupPosition = backupPosition;
        }
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
