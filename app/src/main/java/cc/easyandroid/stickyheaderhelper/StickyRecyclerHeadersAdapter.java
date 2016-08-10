package cc.easyandroid.stickyheaderhelper;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import cc.easyandroid.stickyheaderhelper.dummy.DummyContent;

/**
 * Created by cgpllx on 2016/8/8.
 */
public interface StickyRecyclerHeadersAdapter<VH extends RecyclerView.ViewHolder> {


    VH onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    void onBindHeaderViewHolder(VH holder, int position);

    int getItemCount();

    void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    int getGlobalPositionOf(@NonNull DummyContent.DummyItem item);

    DummyContent.DummyItem getSectionHeader(@IntRange(from = 0) int position);

    int getItemViewType(int position);

    void bindViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    public interface OnStickyHeaderChangeListener {
        /**
         * Called when the current sticky header changed.
         *
         * @param sectionIndex the position of header, -1 if no header is sticky
         * @since 5.0.0-b1
         */
        void onStickyHeaderChange(int sectionIndex);
    }

    ViewGroup getStickySectionHeadersHolder();
}