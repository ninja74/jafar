package iran.com.jafar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import Database.DataSource.tb_JafarDataSource;
import Database.Table.tb_Jafar;

public class InsertActivityJafar extends AppCompatActivity {
    EditText edtCompanyName, edtProductName, edtYear, edtMonth, edtDay, edtKeywords;
    Button btnTakePic, btnSubmit;
    CheckBox chkPayCash, chkCheckNumber, chkBaqi;
    ImageView imvPhoto;
    AlertDialog alertDialogJafar, alertDialogJafar2;
    tb_JafarDataSource tbjafardatasource;
    String intcheckYear = "", intcheckMonth = "", intcheckDays = "", intchechNumber = "", intPaycash = "", intAlbaqi = "";
    String ImageURI = "";
    private static final int CAMERA_PIC_REQUEST = 1111;

    String STedtDay, STedtMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_jafar);

        edtCompanyName = (EditText) findViewById(R.id.edtCompanyName);
        edtProductName = (EditText) findViewById(R.id.edtProductName);
        edtYear = (EditText) findViewById(R.id.edtYear);
        edtMonth = (EditText) findViewById(R.id.edtMonth);
        edtDay = (EditText) findViewById(R.id.edtDay);
        edtKeywords = (EditText) findViewById(R.id.edtKeywords);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnTakePic = (Button) findViewById(R.id.btnTakePic);

        chkBaqi = (CheckBox) findViewById(R.id.chkBaqi);
        chkCheckNumber = (CheckBox) findViewById(R.id.chkCheckNumber);
        chkPayCash = (CheckBox) findViewById(R.id.chkPayCash);

        imvPhoto = (ImageView) findViewById(R.id.imvPhoto);
        imvPhoto.setImageResource(R.mipmap.ic_launcher);

        chkPayCash.setOnClickListener(chkPayCashOnClick);
        chkCheckNumber.setOnClickListener(chkCheckNumberOnClick);
        chkBaqi.setOnClickListener(chkBaqiOnClick);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tbjafardatasource = new tb_JafarDataSource(InsertActivityJafar.this);
                tbjafardatasource.Open();


                tb_Jafar data = new tb_Jafar();

                if (edtCompanyName.getText().toString().compareTo("") == 0 ||
                        edtProductName.getText().toString().compareTo("") == 0 ||
                        edtYear.getText().toString().compareTo("") == 0 ||
                        edtMonth.getText().toString().compareTo("") == 0 ||
                        edtDay.getText().toString().compareTo("") == 0 ||
                        ImageURI.compareTo("") == 0) {
                    Toast.makeText(InsertActivityJafar.this, "لطفا تمام فیلدها را پر کنید!", Toast.LENGTH_SHORT).show();
                } else {


                    if (edtYear.getText().toString().length() != 4 ||
                            Integer.valueOf(edtMonth.getText().toString()) >= 13 ||
                            Integer.valueOf(edtMonth.getText().toString()) <= 0 ||
                            Integer.valueOf(edtDay.getText().toString()) >= 32 ||
                            Integer.valueOf(edtDay.getText().toString()) <= 0
                            ) {
                        Toast.makeText(InsertActivityJafar.this, "لطفا تاریخ را به درستی وارد کنید", Toast.LENGTH_SHORT).show();

                    } else {

                        if (Integer.parseInt(edtMonth.getText().toString()) < 10) {
                            STedtMonth = "0" + edtMonth.getText().toString();
                        } else {
                            STedtMonth = edtMonth.getText().toString();
                        }

                        if (Integer.parseInt(edtDay.getText().toString()) < 10) {
                            STedtDay = "0" + edtDay.getText().toString();
                        } else {
                            STedtDay = edtDay.getText().toString();
                        }


                        data.CompanyName = edtCompanyName.getText().toString();
                        data.productName = edtProductName.getText().toString();
                        data.UrlImg = ImageURI;
                        data.Year = edtYear.getText().toString();
                        data.Month = STedtMonth;
                        data.Days = STedtDay;
                        data.PayCash = intPaycash;
                        data.Baqi = intAlbaqi;
                        data.CheckNumber = intchechNumber;
                        data.CheckYear = intcheckYear;
                        data.CheckMonth = intcheckMonth;
                        data.CheckDays = intcheckDays;
                        data.Keywords = edtKeywords.getText().toString() == "" ? "" : edtKeywords.getText().toString();

                        tbjafardatasource.Add(data);
                        tbjafardatasource.Close();

                        edtCompanyName.setText("");
                        edtProductName.setText("");
                        edtYear.setText("1397");
                        edtMonth.setText("");
                        edtDay.setText("");
                        edtKeywords.setText("");
                        chkBaqi.setChecked(false);
                        chkCheckNumber.setChecked(false);
                        chkPayCash.setChecked(false);
                        imvPhoto.setImageResource(R.mipmap.ic_launcher);

                        Toast.makeText(InsertActivityJafar.this, "اطلاعات با موفقیت ثبت شد!", Toast.LENGTH_SHORT).show();


                    }

                }


            }
        });


        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            if (requestCode == CAMERA_PIC_REQUEST) {


                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                imvPhoto.setImageBitmap(thumbnail);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                thumbnail = Bitmap.createScaledBitmap(thumbnail , thumbnail.getWidth()*3 , thumbnail.getHeight()*3 , false);

                thumbnail.compress(Bitmap.CompressFormat.PNG , 100 , bytes);
                byte[] bitbyte =  bytes.toByteArray();

                String folder_main = "image komijani";

                File f = new File(Environment.getExternalStorageDirectory(), folder_main);
                if (!f.exists()) {
                    f.mkdirs();
                }

                int namePic = 20;
                char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
                StringBuilder sb = new StringBuilder();
                Random random = new Random();
                for (int i = 0; i < namePic; i++) {
                    char c = chars[random.nextInt(chars.length)];
                    sb.append(c);
                }
                String sb1 = "a" + sb;
                String s = sb1 + ".png";

                File file = new File(Environment.getExternalStorageDirectory() + File.separator + folder_main + "/" + s);

                ImageURI = file.toString();
                try {



                    file.createNewFile();
                    FileOutputStream fo = new FileOutputStream(file);
                    fo.write(bitbyte);
                    fo.flush();
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(this, "عکسی گرفته نشد!", Toast.LENGTH_SHORT).show();
        }


    }


    View.OnClickListener chkPayCashOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (chkPayCash.isChecked()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InsertActivityJafar.this);
                RelativeLayout linearLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.item_chk_jafar, null, false);


                final EditText edtAlertdiJafar = (EditText) linearLayout.findViewById(R.id.edtAlertdiJafar);
                final Button btnAlertdiJafar = (Button) linearLayout.findViewById(R.id.btnAlertdiJafar);
                final TextView txtAlertdiJafar = (TextView) linearLayout.findViewById(R.id.txtAlertdiJafar);

                txtAlertdiJafar.setText("لطفا قیمت را وارد کنید: ");
                btnAlertdiJafar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chkPayCash.setText(edtAlertdiJafar.getText().toString());
                        if (edtAlertdiJafar.getText().toString().compareTo("") == 0) {
                            chkPayCash.setText("پرداخت نقدی");
                            chkPayCash.setChecked(false);
                            alertDialogJafar.dismiss();
                        } else {
                            alertDialogJafar.dismiss();
                            intPaycash = edtAlertdiJafar.getText().toString();

                        }
                    }
                });
                builder.setCancelable(false);
                builder.setView(linearLayout);
                alertDialogJafar = builder.create();
                alertDialogJafar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialogJafar.show();


            } else {
                chkPayCash.setText("پرداخت نقدی");
            }
        }
    };

    View.OnClickListener chkCheckNumberOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (chkCheckNumber.isChecked()) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(InsertActivityJafar.this);
                RelativeLayout linearLayout2 = (RelativeLayout) getLayoutInflater().inflate(R.layout.item_chk_jafar2, null, false);

                final EditText edtAlertdiJafar2 = (EditText) linearLayout2.findViewById(R.id.edtAlertdiJafar2);
                final EditText edtDaychk2 = (EditText) linearLayout2.findViewById(R.id.edtDaychk2);
                final EditText edtMonthchk2 = (EditText) linearLayout2.findViewById(R.id.edtMonthchk2);
                final EditText edtYearchk2 = (EditText) linearLayout2.findViewById(R.id.edtYearchk2);
                Button btnAlertdiJafar2 = (Button) linearLayout2.findViewById(R.id.btnAlertdiJafar2);
                TextView txtAlertdiJafar2 = (TextView) linearLayout2.findViewById(R.id.txtAlertdiJafar2);
                final LinearLayout linCkeckDate2 = (LinearLayout) linearLayout2.findViewById(R.id.linCkeckDate2);
                linCkeckDate2.setVisibility(View.VISIBLE);

                txtAlertdiJafar2.setText("چک به شماره: ");
                btnAlertdiJafar2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chkCheckNumber.setText(
                                "چک به شماره ی: "
                                        + edtAlertdiJafar2.getText().toString() +
                                        " به تاریخ: " +
                                        edtYearchk2.getText().toString() + "/" +
                                        edtMonthchk2.getText().toString() + "/" +
                                        edtDaychk2.getText().toString()
                        );


                        if (edtAlertdiJafar2.getText().toString().compareTo("") == 0 || edtYearchk2.getText().toString().compareTo("") == 0 || edtMonthchk2.getText().toString().compareTo("") == 0 || edtDaychk2.getText().toString().compareTo("") == 0) {
                            chkCheckNumber.setText("چک به شماره و تاریخ");
                            chkCheckNumber.setChecked(false);
                            alertDialogJafar2.dismiss();
                        } else {
                            intcheckYear = edtYearchk2.getText().toString();
                            intcheckMonth = edtMonthchk2.getText().toString();
                            intcheckDays = edtDaychk2.getText().toString();
                            intchechNumber = edtAlertdiJafar2.getText().toString();
                            alertDialogJafar2.dismiss();
                        }
                    }
                });


                builder2.setCancelable(false);
                builder2.setView(linearLayout2);
                alertDialogJafar2 = builder2.create();
                alertDialogJafar2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialogJafar2.show();


            } else {
                chkCheckNumber.setText("چک به شماره و تاریخ");
            }


        }
    };

    View.OnClickListener chkBaqiOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (chkBaqi.isChecked()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InsertActivityJafar.this);
                RelativeLayout linearLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.item_chk_jafar, null, false);

                final EditText edtAlertdiJafar = (EditText) linearLayout.findViewById(R.id.edtAlertdiJafar);
                Button btnAlertdiJafar = (Button) linearLayout.findViewById(R.id.btnAlertdiJafar);
                TextView txtAlertdiJafar = (TextView) linearLayout.findViewById(R.id.txtAlertdiJafar);

                txtAlertdiJafar.setText("الباقی: ");
                btnAlertdiJafar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chkBaqi.setText(edtAlertdiJafar.getText().toString());
                        if (edtAlertdiJafar.getText().toString().compareTo("") == 0) {
                            chkBaqi.setText("الباقی");
                            chkBaqi.setChecked(false);
                            alertDialogJafar.dismiss();
                        } else {
                            intAlbaqi = edtAlertdiJafar.getText().toString();
                            alertDialogJafar.dismiss();
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setView(linearLayout);
                alertDialogJafar = builder.create();
                alertDialogJafar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialogJafar.show();


            } else {
                chkBaqi.setText("الباقی");
            }
        }
    };


}

