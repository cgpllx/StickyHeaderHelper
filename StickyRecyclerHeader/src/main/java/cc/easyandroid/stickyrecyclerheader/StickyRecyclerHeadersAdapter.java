package cc.easyandroid.stickyrecyclerheader;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by cgpllx on 2016/8/8.
 */
public interface StickyRecyclerHeadersAdapter<VH extends RecyclerView.ViewHolder> {
    VH onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    int getItemCount();

    int getGlobalPositionOf(@NonNull Object item);

    Object getSectionHeader(@IntRange(from = 0) int position);

    int getItemViewType(int position);

    void bindViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    RecyclerView getRecyclerView();

    ViewGroup getStickySectionHeadersHolder();

    void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position);

    interface OnStickyHeaderChangeListener {//sticky header 改变监听

        void onStickyHeaderChange(int sectionIndex);
    }


    class StickViewHolder extends RecyclerView.ViewHolder {
        private View contentView;
        private int mBackupPosition = RecyclerView.NO_POSITION;

        public StickViewHolder(View item, StickyRecyclerHeadersAdapter adapter) {
            super(new FrameLayout(item.getContext()));
            itemView.setLayoutParams(adapter.getRecyclerView().getLayoutManager()
                    .generateLayoutParams(item.getLayoutParams()));
            ((FrameLayout) itemView).addView(item);//Add View after setLayoutParams
            contentView = itemView;
        }

        public View getContentView() {
            return contentView != null ? contentView : itemView;
        }

        public int getFlexibleAdapterPosition() {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                position = mBackupPosition;
            }
            return position;
        }

        public void setBackupPosition(int backupPosition) {
            mBackupPosition = backupPosition;
        }
    }
}