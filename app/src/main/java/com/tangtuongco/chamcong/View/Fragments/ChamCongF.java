package com.tangtuongco.chamcong.View.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tangtuongco.chamcong.Model.GioCong;
import com.tangtuongco.chamcong.Model.NhanVien;
import com.tangtuongco.chamcong.R;
import com.tangtuongco.chamcong.Ulty.FormatHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class ChamCongF extends Fragment {
    Button checkin,checkout;
    TextView txtNgay,txtIn,txtOut,txtTinhToan;
    FirebaseAuth mAuth;
    DatabaseReference data;
    EditText edtOTP;
    FirebaseDatabase firebaseDatabase;
    String email;
    NhanVien currentNv;
    GioCong gioCong;
    ProgressDialog progressDialog;
    LinearLayout layoutOTP;
    Long OTPPP;
    Button btnOTP;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_cham_cong,container,false);
        anhxa(view);

        capnhat();
        //Lay data
        //Lay email;
        email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        firebaseDatabase=FirebaseDatabase.getInstance();
        currentNv=new NhanVien();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        getData();
        //Check-in

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickCheckIn();
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCheckOut();
            }
        });






        return view;
    }



    private void clickCheckOut() {
        gioCong=new GioCong();
        final Date ngaycheckin= Calendar.getInstance().getTime();
        final int thang=Calendar.getInstance().get(Calendar.MONTH);
        final int ngay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        long a=00;
        String currentNgay = FormatHelper.formatNgay(ngaycheckin);
        String currentGio=FormatHelper.formatGio(ngaycheckin);

        data=firebaseDatabase.getReference().child("GioCong").child(String.valueOf(thang)).child(String.valueOf(ngay));
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    GioCong a = dataSnapshot1.getValue(GioCong.class);
                    if(a.getClickOut()==false)
                    {
                        a.setGioRa(FormatHelper.formatGio(ngaycheckin));
                        txtOut.setText(FormatHelper.formatGio(ngaycheckin));
                        a.setClickOut(true);
                        data.child(currentNv.getManv()).setValue(a);
                        Toasty.info(getActivity(),"Bạn đã check out vào " + a.getGioRa(),Toast.LENGTH_SHORT).show();
                        data=firebaseDatabase.getReference().child("GioCong");
                    }
                    else
                    {
                        Toasty.warning(getActivity(),"Bạn đã check out rồi",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void ClickCheckIn() {
        gioCong=new GioCong();
        Date ngaycheckin= Calendar.getInstance().getTime();
        int thang=Calendar.getInstance().get(Calendar.MONTH);
        int ngay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        long a=00;
        String currentNgay = FormatHelper.formatNgay(ngaycheckin);
        String currentGio=FormatHelper.formatGio(ngaycheckin);
//        data=firebaseDatabase.getReference().child("GioCong");

        data=firebaseDatabase.getReference().child("OTP").child(currentNv.getManv());
        data.setValue(random());
        layoutOTP.setVisibility(View.VISIBLE);
        getOTP();
        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("kiemtra",OTPPP+"");
                //Kiem tra OTP nhap vao
                if(edtOTP.getText().toString().equals(OTPPP.toString()))
                {

                    Toast.makeText(getActivity(), "TRUEEEEEE", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(), "FALSEe", Toast.LENGTH_SHORT).show();
                }
            }
        });




//        if(gioCong.getClickIn()==false)
//        {
//            gioCong.setGioVao(currentGio);
//            gioCong.setGioRa("00");
//            gioCong.setNgay(currentNgay);
//            gioCong.setClickIn(true);
//            data=firebaseDatabase.getReference().child("GioCong");
//            data.child(String.valueOf(thang)).child(String.valueOf(ngay)).child(currentNv.getManv()).setValue(gioCong);
//            Toasty.info(getActivity(),"Bạn đã check in vào " + currentGio,Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Toasty.warning(getActivity(),"Bạn đã check in rồi",Toast.LENGTH_SHORT).show();
//        }



    }

    private void getOTP() {
        data=firebaseDatabase.getReference().child("OTP").child(currentNv.getManv());
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long OTP = (Long) dataSnapshot.getValue();
                saveOTP(OTP);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private int random()
    {
        int min=0;
        int max=9999;
        int random=new Random().nextInt((max-min)+1);
        return random;
    }
    private void getData() {
        data=firebaseDatabase.getReference().child("NhanVien");

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    NhanVien a = dataSnapshot1.getValue(NhanVien.class);
                    if(a.getEmail().equals(email))
                    {
                       saveNV(a);
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void saveNV(NhanVien a) {
        currentNv=a;
    }
    private void saveOTP(Long otp) {
        OTPPP=otp;
    }

    private void capnhat() {
        Calendar c = Calendar.getInstance();
        Date ngay = c.getTime();
        txtNgay.setText(FormatHelper.formatNgay(ngay));
        txtIn.setText(FormatHelper.formatGio(ngay));
    }

    private void anhxa(View view) {
        checkin=view.findViewById(R.id.btnCheckInCC);
        checkout=view.findViewById(R.id.btnCheckOutCC);
        txtNgay=view.findViewById(R.id.txtNgayCC);
        txtIn=view.findViewById(R.id.txtCheckInCC);
        txtOut=view.findViewById(R.id.txtCheckOutCC);
        txtTinhToan=view.findViewById(R.id.txtSoGioLamCC);
        layoutOTP=view.findViewById(R.id.LinearOTP);
        edtOTP=view.findViewById(R.id.edtOTP);
        btnOTP=view.findViewById(R.id.btnOTP);
    }
}
