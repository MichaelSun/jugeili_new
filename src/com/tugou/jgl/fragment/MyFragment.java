package com.tugou.jgl.fragment;

import com.tugou.jgl.R;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class MyFragment extends Fragment {
    @Override  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //mInflater = inflater;
        View ret = inflater.inflate(R.layout.my, null);

        //test
//        ret.findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent();
//                i.setClass(getActivity().getApplicationContext(), SubListActivity.class);
//                getActivity().startActivity(i);
//            }
//        });
        GridView gv = (GridView)ret.findViewById(R.id.category);
        //gv.setAdapter(new CategoryAdapter());

        return ret;
    }
}
