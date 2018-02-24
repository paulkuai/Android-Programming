package com.example.paul.myandroidparctice;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String ARG_POSITION = "position";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO = 2;

    final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
    Intent dial;
    final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    boolean canTakePhoto;

    @BindView(R.id.crime_title)
    EditText mTitleField;
    @BindView(R.id.crime_date)
    Button mDateButton;
    @BindView(R.id.crime_solved)
    CheckBox mSolvedCheckBox;
    Unbinder unbinder;
    @BindView(R.id.crime_suspect)
    Button mCrimeSuspect;
    @BindView(R.id.crime_report)
    Button mCrimeReport;
    @BindView(R.id.crime_dial)
    Button mCrimeDial;
    @BindView(R.id.crime_photo)
    ImageView mCrimePhoto;
    @BindView(R.id.crime_camera)
    ImageButton mCrimeCamera;
    private Crime mCrime;
    private int mPosition;

    private int contactId;
    private String phoneNumber;
    private File mPhotoFile;


    public static CrimeFragment newInstance(UUID crimeId, int position) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        args.putSerializable(ARG_POSITION, position);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mCrime = new Crime();
//        UUID crimeId = (UUID)getActivity().getIntent()
//                .getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPosition = (int) getArguments().getSerializable(ARG_POSITION);
        setHasOptionsMenu(true);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        unbinder = ButterKnife.bind(this, v);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
                returnResult();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //This one too
            }
        });
        updateDate();
        //mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
//                DatePickerFragment dialog = new DatePickerFragment();
//                DatePickerFragment dialog = DatePickerFragment
//                        .newInstance(mCrime.getDate());
//                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
//                dialog.show(manager,DIALOG_DATE);
                Intent intent = DatePickerActivity.newIntent(getActivity(), mCrime.getDate());
                startActivityForResult(intent, REQUEST_DATE);
            }
        });
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
                returnResult();
            }
        });

        if (mCrime.getSuspect() != null) {
            mCrimeSuspect.setText(mCrime.getSuspect());
        }

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS},
                1);
        canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(getActivity().getPackageManager()) != null;
        updatePhotoView();
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.del_crime:
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void returnResult() {
        Intent data = new Intent();
        data.putExtra(ARG_CRIME_ID, mCrime.getId());
        data.putExtra(ARG_POSITION, mPosition);
        getActivity().setResult(Activity.RESULT_OK, data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts._ID
            };
            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);

            try {
                if (c.getCount() == 0) {
                    return;
                }

                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mCrimeSuspect.setText(suspect);
                contactId = c.getInt(1);

            } finally {
                c.close();
            }
        }else if(requestCode == REQUEST_PHOTO)
        {
            Uri uri = FileProvider.getUriForFile(getActivity(),"com.example.paul.myandroidparctice.criminalintent.fileprovider",mPhotoFile);
            getActivity().revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }

    @OnClick({R.id.crime_suspect, R.id.crime_report, R.id.crime_dial,R.id.crime_camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.crime_suspect:
                startActivityForResult(pickContact, REQUEST_CONTACT);
                break;
            case R.id.crime_report:
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
//                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
//                i = Intent.createChooser(i,getString(R.string.send_report));
//                startActivity(i);
                ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(getActivity());
                builder.setType("text/plain");
                builder.setText(getCrimeReport());
                builder.setSubject(getString(R.string.crime_report_subject));
                builder.setChooserTitle(R.string.send_report);
                builder.startChooser();
                break;
            case R.id.crime_dial:
                Cursor c = getActivity().getContentResolver()
                        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

                try {
                    if (c.getCount() == 0) {
                        return;
                    }

                    c.moveToFirst();
                    phoneNumber = c.getString(0);

                } finally {
                    c.close();
                }
                dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                startActivity(dial);
                break;
            case R.id.crime_camera:
                Uri uri = FileProvider.getUriForFile(getActivity(),"com.example.paul.myandroidparctice.criminalintent.fileprovider",mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,PackageManager.MATCH_DEFAULT_ONLY);
                for(ResolveInfo activity:cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName,uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(captureImage,REQUEST_PHOTO);
                }
                break;
            default:
                break;
        }
    }

    private void updatePhotoView(){
        if(mPhotoFile == null||!mPhotoFile.exists()){
            mCrimePhoto.setImageDrawable(null);
        }else{
            Bitmap bitmap = PicureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            mCrimePhoto.setImageBitmap(bitmap);
        }
    }

}
