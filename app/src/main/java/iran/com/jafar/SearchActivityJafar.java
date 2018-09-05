package iran.com.jafar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Database.DataSource.tb_JafarDataSource;
import Database.Table.tb_Jafar;

public class SearchActivityJafar extends AppCompatActivity {

    String STedtDaySE = "", STedtMonthSE = "";
    ListView lstResSE;
    AlertDialog alertDialogJafarShowPost;
    List<tb_Jafar> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_jafar);

        Button btnResult = (Button) findViewById(R.id.btnResult);
        final TextView txtRes = (TextView) findViewById(R.id.txtRes);
        final EditText edtCompanySearch = (EditText) findViewById(R.id.edtCompanySearch);
        final EditText edtProductSearch = (EditText) findViewById(R.id.edtProductSearch);
        final EditText edtYearSearch = (EditText) findViewById(R.id.edtYearSearch);
        final EditText edtMonthSearch = (EditText) findViewById(R.id.edtMonthSearch);
        final EditText edtDaysSearch = (EditText) findViewById(R.id.edtDaysSearch);
        final EditText edtKeywordsSearch = (EditText) findViewById(R.id.edtKeywordsSearch);
        lstResSE = (ListView) findViewById(R.id.lstResSE);




        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if (!(edtMonthSearch.getText().toString().equals(""))) {
                    if (Integer.parseInt(edtMonthSearch.getText().toString()) < 10) {
                        STedtMonthSE = "0" + edtMonthSearch.getText().toString();
                    } else {
                        STedtMonthSE = edtMonthSearch.getText().toString();
                    }

                }


                if (!(edtDaysSearch.getText().toString().equals(""))) {
                    if (Integer.parseInt(edtDaysSearch.getText().toString()) < 10) {
                        STedtDaySE = "0" + edtDaysSearch.getText().toString();
                    } else {
                        STedtDaySE = edtDaysSearch.getText().toString();
                    }

                }


                tb_JafarDataSource tb_jafarDataSource = new tb_JafarDataSource(SearchActivityJafar.this);

                tb_jafarDataSource.Open();

                data = tb_jafarDataSource.GetRecordByCompanyName1(edtCompanySearch.getText().toString().equals("") ? null : edtCompanySearch.getText().toString(),
                        edtProductSearch.getText().toString().equals("") ? null : edtProductSearch.getText().toString(),
                        edtYearSearch.getText().toString().equals("") ? null : edtYearSearch.getText().toString(),
                        STedtMonthSE.equals("") ? null : STedtMonthSE,
                        STedtDaySE.equals("") ? null : STedtDaySE,
                        edtKeywordsSearch.getText().toString().equals("") ? null : edtKeywordsSearch.getText().toString()
                );




                if (data != null){
                    try {
                        UserList listViewPersonAdapter = new UserList(SearchActivityJafar.this,data , R.layout.customelistview, clickInterface);
                        lstResSE.setAdapter(listViewPersonAdapter);
                    }catch (Exception e){
                        Log.d("Err" , e.getMessage());
                    }
                }else {
                    lstResSE.removeAllViewsInLayout();
                    Toast.makeText(SearchActivityJafar.this, "هیچ دیتایی وجود ندارد!", Toast.LENGTH_SHORT).show();
                }









//
//                String aa = "";
//
//                txtRes.setText(aa);
//
//
//                if (data != null) {
//                    try {
//                        for (tb_Jafar dat : data) {
//                            aa = aa + dat.PK_Jafar + "\n";
//                            txtRes.setText(aa);
//                        }
//                    } catch (Exception e) {
//                        Log.d("Err", e.getMessage());
//                    }
//                } else {
//                    Toast.makeText(SearchActivityJafar.this, "هیچ دیتایی وجود ندارد!", Toast.LENGTH_SHORT).show();
//                }

                tb_jafarDataSource.Close();

            }
        });

    }

    UserList.imagetxtInterFace clickInterface = new UserList.imagetxtInterFace() {
        @Override
        public void SelectedItemCallback(tb_Jafar data) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivityJafar.this);
            LinearLayout linearLayoutShow = (LinearLayout) getLayoutInflater().inflate(R.layout.show_post_image, null, false);

            TextView txtNameComShow = (TextView) linearLayoutShow.findViewById(R.id.txtNameComShow);
            TextView txtProNameShow = (TextView) linearLayoutShow.findViewById(R.id.txtProNameShow);
            TextView txtDateShow = (TextView) linearLayoutShow.findViewById(R.id.txtDateShow);
            TextView txtPayCashShow = (TextView) linearLayoutShow.findViewById(R.id.txtPayCashShow);
            TextView txtCheckNumShow = (TextView) linearLayoutShow.findViewById(R.id.txtCheckNumShow);
            TextView txtAlbaqiShow = (TextView) linearLayoutShow.findViewById(R.id.txtAlbaqiShow);
            TextView txtKeyWordsShow = (TextView) linearLayoutShow.findViewById(R.id.txtKeyWordsShow);
            ImageView imgFactorShow = (ImageView) linearLayoutShow.findViewById(R.id.imgFactorShow);
            LinearLayout linerImageFac = (LinearLayout) linearLayoutShow.findViewById(R.id.linerImageFac);
            LinearLayout linerPost = (LinearLayout) linearLayoutShow.findViewById(R.id.linerPost);

            linerImageFac.setVisibility(View.GONE);
            linerPost.setVisibility(View.VISIBLE);

            txtNameComShow.setText("نام شرکت: " + data.CompanyName);
            txtProNameShow.setText("نام محصول: " + data.productName);
            txtDateShow.setText("تاریخ: " + data.Year + "/" + data.Month + "/" + data.Days);
            txtPayCashShow.setText(data.PayCash.equals("") ? "پرداخت نقدی: ندارد" : "پرداخت نقدی: " + data.PayCash);
            txtCheckNumShow.setText(data.CheckNumber.equals("") ? "چک: ندارد" : "شماره چک: " + data.CheckNumber + " تاریخ چک: " + data.CheckYear + "/" + data.CheckMonth + "/" + data.CheckDays);
            txtAlbaqiShow.setText(data.Baqi.equals("") ? "الباقی: ندارد" : "الباقی: " + data.Baqi);
            txtKeyWordsShow.setText("کلمات کلیدی: " + data.Keywords);

            Bitmap neadsf = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(data.UrlImg), 1500, 1500, false);
            imgFactorShow.setImageBitmap(neadsf);

            builder.setView(linearLayoutShow);
            alertDialogJafarShowPost = builder.create();
            alertDialogJafarShowPost.show();

        }

        @Override
        public void ImageFactorCallBack(String imgUri) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivityJafar.this);
            LinearLayout linearLayoutShow = (LinearLayout) getLayoutInflater().inflate(R.layout.show_post_image, null, false);

            final ImageView imvPhotoFactorShowLarg = (ImageView) linearLayoutShow.findViewById(R.id.imvPhotoFactorShowLarg);
            LinearLayout linerImageFac = (LinearLayout) linearLayoutShow.findViewById(R.id.linerImageFac);
            LinearLayout linerPost = (LinearLayout) linearLayoutShow.findViewById(R.id.linerPost);

            linerImageFac.setVisibility(View.VISIBLE);
            linerPost.setVisibility(View.GONE);

            Bitmap neadsf = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgUri), 1500, 1500, false);

            imvPhotoFactorShowLarg.setImageBitmap(neadsf);
            //     imvPhotoFactorShowLarg.setImageBitmap(BitmapFactory.decodeFile(imgUri));


            builder.setView(linearLayoutShow);
            alertDialogJafarShowPost = builder.create();
            alertDialogJafarShowPost.show();
        }
    };
}
