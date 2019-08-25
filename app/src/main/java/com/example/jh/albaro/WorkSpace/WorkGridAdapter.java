package com.example.jh.albaro.WorkSpace;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jh.albaro.R;

import java.util.ArrayList;

public class WorkGridAdapter extends BaseAdapter {

    private ArrayList<WorkGridListItem> gridViewItemList = new ArrayList<WorkGridListItem>();
    private boolean mClick = false;
    protected int selectedRow  = -1;

    public WorkGridAdapter(){

    }

    @Override
    public int getCount() {
        return gridViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return gridViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(int num, String store_id, String store_name, String Is_admin) {
        WorkGridListItem item = new WorkGridListItem();
        item.setNum(num);
        item.setStore_id(store_id);
        item.setStore_name(store_name);
        item.setIs_admin(Is_admin);

        gridViewItemList.add(item);
    }


    public void showDeleteButton() {
        mClick = !mClick;
    } //end showDeleteButton()

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.workspace_gridview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        final ImageView bt_delete = convertView.findViewById(R.id.bt_delete);
        final ImageView building = convertView.findViewById(R.id.ib_building);
        final TextView name = convertView.findViewById(R.id.tv_name);
        final LinearLayout background = convertView.findViewById(R.id.ll_background);


        final WorkGridListItem gridViewItem = gridViewItemList.get(position);

        name.setText(gridViewItem.getStore_name());

        //if delete button click
        if(mClick){
            bt_delete.setVisibility(View.VISIBLE);
        }else{
            bt_delete.setVisibility(View.INVISIBLE);
        }

        if(selectedRow==position){
            building.setBackground(ContextCompat.getDrawable(context, R.drawable.buliding_white));
            name.setTextColor(ContextCompat.getColor(context, R.color.white));
            background.setBackgroundColor(ContextCompat.getColor(context, R.color.base_color));
            bt_delete.setBackground(ContextCompat.getDrawable(context, R.drawable.delete_white));

        }else{
            building.setBackground(ContextCompat.getDrawable(context, R.drawable.building_mint));
            name.setTextColor(ContextCompat.getColor(context, R.color.base_color));
            background.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_stroke_basecolor_2dp));
            bt_delete.setBackground(ContextCompat.getDrawable(context, R.drawable.delete_mint));
        }


        //클릭 이벤트 지정
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeCheck_Dialog customDialog = new removeCheck_Dialog(context, gridViewItem.getNum());
                customDialog.callFunction();
                customDialog.setDialogListener(new removeCheck_Dialog.CustomDialogListener() {
                    @Override
                    public void onPositiveClicked(int result) {
                        if(result==1){
                            gridViewItemList.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                });


            }
        });


        return convertView;
    }



}