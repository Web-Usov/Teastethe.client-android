package ru.myteas.teaclient_a.Teas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.myteas.teaclient_a.R;

public class TeaAdapter extends BaseAdapter {

    private List<Tea> list;
    private LayoutInflater layoutInflater;

    public TeaAdapter(Context context, List<Tea> list) {
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = layoutInflater.inflate(R.layout.tea_item,parent, false);
        }

        Tea tea = getTea(position);

        TextView teaItemTitle = view.findViewById(R.id.teaItemTitle);
        teaItemTitle.setText(tea.getName()+" - "+tea.getType());
        TextView teaItemRating = view.findViewById(R.id.teaItemRating);
        teaItemRating.setText(String.valueOf(tea.getRating()));

        TextView teaItemSubtitle = view.findViewById(R.id.teaItemSubtitle);
        teaItemSubtitle.setText("Added by: "+tea.getParent().getName());

        return view;
    }

    private Tea getTea(int position){

        return (Tea) getItem(position);
    }
}
