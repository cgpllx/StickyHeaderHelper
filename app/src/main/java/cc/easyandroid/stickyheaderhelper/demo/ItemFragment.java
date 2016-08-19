package cc.easyandroid.stickyheaderhelper.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cc.easyandroid.easyrecyclerview.EasyRecyclerAdapter;
import cc.easyandroid.easyrecyclerview.EasyRecyclerView;
import cc.easyandroid.easyrecyclerview.core.DefaultFooterHander;
import cc.easyandroid.easyrecyclerview.core.DefaultHeaderHander;
import cc.easyandroid.stickyheaderhelper.demo.dummy.DummyContent;

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
        List<DummyContent.DummyItem> items = new ArrayList<>();
        items.add(0, new DummyContent.DummyItem("dd", "content111 ", "dddff", true));
        items.add(new DummyContent.DummyItem("dd", "content222 ", "dddff", true));
        items.addAll(DummyContent.ITEMS);
        items.add(new DummyContent.DummyItem("dd", "content222 ", "dddff", true));
        items.addAll(DummyContent.getItems());
        items.add(new DummyContent.DummyItem("dd", "content333 ", "dddff", true));
        items.addAll(DummyContent.getItems());
        items.add(new DummyContent.DummyItem("dd", "content444 ", "dddff", true));
        items.addAll(DummyContent.ITEMS);
        items.add(new DummyContent.DummyItem("dd", "content555 ", "dddff", true));
        items.addAll(DummyContent.getItems());
        final MyItemRecyclerViewAdapter adapter = new  MyItemRecyclerViewAdapter(items).setStickyHeaders(true);

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new EasyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int i) {
                if (toast == null) {
                    toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
                }
                EasyRecyclerAdapter adapter1=null;
//                DummyContent.DummyItem dummyItem=  adapter1.getData(i);
                toast.setText("i=" + i + "  ---  " + "dummyItem=" + adapter.getData(i) );
//                toast.setText("i=" + i + "  ---  " + "dummyItem=" + adapter.getData(i).content);
                toast.show();
            }
        });
    }
}
