package cc.easyandroid.stickyheaderhelper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cc.easyandroid.easyrecyclerview.EasyRecyclerAdapter;
import cc.easyandroid.easyrecyclerview.EasyRecyclerView;
import cc.easyandroid.easyrecyclerview.core.DefaultFooterHander;
import cc.easyandroid.easyrecyclerview.core.DefaultHeaderHander;
import cc.easyandroid.stickyheaderhelper.dummy.DummyContent;

public class ItemFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    Toast toast;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toast = Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT);
        Context context = view.getContext();
        EasyRecyclerView recyclerView = (EasyRecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setHeaderHander(new DefaultHeaderHander(getContext()));
        recyclerView.setFooterHander(new DefaultFooterHander(getContext()));
        MyItemRecyclerViewAdapter adapter = new MyItemRecyclerViewAdapter(DummyContent.ITEMS).setStickyHeaders(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new EasyRecyclerAdapter.OnItemClickListener<DummyContent.DummyItem>() {
            @Override
            public void onItemClick(int i, DummyContent.DummyItem dummyItem) {
                if (toast == null) {
                    toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
                }
                toast.setText("i=" + i);
                toast.show();
            }
        });
    }
}
