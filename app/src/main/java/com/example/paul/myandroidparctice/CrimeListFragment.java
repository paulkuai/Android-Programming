package com.example.paul.myandroidparctice;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CrimeListFragment extends Fragment {

    private static final int REQUEST_CRIME = 1;
    @BindView(R.id.crime_recycler_view)
    RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    Unbinder unbinder;
    private boolean mSubtitleVisible;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        unbinder = ButterKnife.bind(this, v);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntext(getActivity(),crime.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CRIME) {
            if (data != null) {
                mCrimeAdapter.notifyItemChanged(data.getIntExtra("position", 0));
            }
        }
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mCrimeAdapter);
        } else {
            mCrimeAdapter.setCrimes(crimes);
            mCrimeAdapter.notifyDataSetChanged();
        }
        updateSubtitle();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format,crimeCount);

        if(!mSubtitleVisible){
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    public class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        @BindView(R.id.crime_title)
//        TextView mCrimeTitle;
//        @BindView(R.id.crime_date)
//        TextView mCrimeDate;
        TextView mCrimeTitle;
        TextView mCrimeDate;
        ImageView mSolvedImageView;

        private Crime mCrime;

        @Override
        public void onClick(View view) {
            //Toast.makeText(getActivity(), mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(getActivity(),CrimeActivity.class);
            //Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId(), mCrimeRecyclerView.getChildAdapterPosition(itemView));
            Intent intent = CrimePagerActivity.newIntext(getActivity(), mCrime.getId(), mCrimeRecyclerView.getChildAdapterPosition(itemView));
            startActivityForResult(intent, REQUEST_CRIME);
        }

        public CrimeHolder(View view) {
            super(view);
            //super(inflater.inflate(R.layout.list_item_crime, parent, false));
//            ButterKnife.bind(this, parent);
            mCrimeTitle = (TextView) itemView.findViewById(R.id.crime_title);
            mCrimeDate = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
            itemView.setOnClickListener(this);
        }

        private void bind(Crime crime) {
            mCrime = crime;
            mCrimeTitle.setText(mCrime.getTitle());
            mCrimeDate.setText(DateFormat.format("EEEE,MMM dd,yyyy", mCrime.getDate()));
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
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
            mCrimeDate = (TextView) itemView.findViewById(R.id.crime_date);
            mBtnCallPolice = (Button) itemView.findViewById(R.id.call_police);
        }

        private void bind(Crime crime) {
            mCrime = crime;
            mCrimeTitle.setText(mCrime.getTitle());
            mCrimeDate.setText(DateFormat.format("EEEE,MMM dd,yyyy", mCrime.getDate()));
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
            return new CrimeHolder(inflater.inflate(R.layout.list_item_crime, parent, false));
//            if (viewType == 1) {
//                return new CrimeHolder(inflater.inflate(R.layout.list_item_crime, parent, false));
//            } else {
//                return new CrimePoliceHolder(inflater.inflate(R.layout.list_item_crime_police, parent, false));
//            }
        }

        @Override
        public int getItemViewType(int position) {
            return mCrimes.get(position).getRequiresPolice();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            if (holder instanceof CrimeHolder) {
                ((CrimeHolder) holder).bind(crime);
            } else if (holder instanceof CrimePoliceHolder) {
                ((CrimePoliceHolder) holder).bind(crime);
            }

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes){
            mCrimes = crimes;
        }
    }

}
