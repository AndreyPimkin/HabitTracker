package penza.it.habittracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class PersonNoAuthoFragment extends Fragment {
    private SettingAdapter settingAdapter;
    private ArrayList<String> settingList = new ArrayList<>(10);
    private ArrayList<String> imageList = new ArrayList<>(10);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settingList.add("Смена темы");
        settingList.add("Языковые параметры");
        settingList.add("Уведомления");

        imageList.add("change_themes");
        imageList.add("language");
        imageList.add("notification");


    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_no_autho, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listSettingNo);
        settingAdapter = new SettingAdapter(getActivity());
        listView.setAdapter(settingAdapter);
        return view;
    }
    private class SettingAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;

        SettingAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return settingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("DiscouragedApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = mLayoutInflater.inflate(R.layout.person_design, null);

            ImageView image = (ImageView) convertView.findViewById(R.id.imageSetting);
            image.setImageResource(getResources().getIdentifier(imageList.get(position), "drawable", requireActivity().getPackageName()));

            TextView signTextView = (TextView) convertView.findViewById(R.id.nameSetting);
            signTextView.setText(settingList.get(position));


            return convertView;
        }
    }



}