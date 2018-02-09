package com.example.paul.myandroidparctice;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CrimeListFragment extends Fragment {
    @BindView(R.id.crime_recycler_view)
    RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        unbinder = ButterKnife.bind(this, v);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        mCrimeAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mCrimeAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        @BindView(R.id.crime_title)
//        TextView mCrimeTitle;
//        @BindView(R.id.crime_date)
//        TextView mCrimeDate;
        TextView mCrimeTitle;
        TextView mCrimeDate;

        private Crime mCrime;

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), mCrime.getTitle()+" clicked!", Toast.LENGTH_SHORT).show();
        }

        public CrimeHolder(View view) {
            super(view);
            //super(inflater.inflate(R.layout.list_item_crime, parent, false));
//            ButterKnife.bind(this, parent);
            mCrimeTitle = (TextView) itemView.findViewById(R.id.crime_title);
            mCrimeDate = (TextView)itemView.findViewById(R.id.crime_date);
            itemView.setOnClickListener(this);
        }

        private void bind(Crime crime){
            mCrime = crime;
            mCrimeTitle.setText(mCrime.getTitle());
            mCrimeDate.setText(mCrime.getDate().toString());
        }

    }

    public class CrimePoliceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        @BindView(R.id.crime_title)
//        TextView mCrimeTitle;
//        @BindView(R.id.crime_date)
//        TextView mCrimeDate;
        TextView mCrimeTitle;
        TextView mCrimeDate;
        Button mBtnCallPolice;

        private Crime mCrime;

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "calling police,hold on please...", Toast.LENGTH_SHORT).show();
        }

        public CrimePoliceHolder(View view) {
            super(view);
            //super(inflater.inflate(R.layout.list_item_crime, parent, false));
//            ButterKnife.bind(this, parent);
            mCrimeTitle = (TextView) itemView.findViewById(R.id.crime_title);
            mCrimeDate = (TextView)itemView.findViewById(R.id.crime_date);
            mBtnCallPolice = (Button)itemView.findViewById(R.id.call_police);
        }

        private void bind(Crime crime){
            mCrime = crime;
            mCrimeTitle.setText(mCrime.getTitle());
            mCrimeDate.setText(mCrime.getDate().toString());
            mBtnCallPolice.setOnClickListener(this);
        }

    }



    public class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            if(viewType == 1){
                return new CrimeHolder(inflater.inflate(R.layout.list_item_crime,parent, false));
            }else{
                return new CrimePoliceHolder(inflater.inflate(R.layout.list_item_crime_police,parent, false));
            }
        }

        @Override
        public int getItemViewType(int position) {
            return mCrimes.get(position).getRequiresPolice();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            if(holder instanceof CrimeHolder){
                ((CrimeHolder)holder).bind(crime);
            }else if(holder instanceof  CrimePoliceHolder){
                ((CrimePoliceHolder)holder).bind(crime);
            }

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

}