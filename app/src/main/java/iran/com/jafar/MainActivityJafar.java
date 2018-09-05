package iran.com.jafar;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

import Database.DataSource.tb_JafarDataSource;
import Database.DatabaseManagement;
import Database.Structure.tb_JafarStructure;
import Database.Table.tb_Jafar;


public class MainActivityJafar extends AppCompatActivity {

    AlertDialog alertDialogJafarShowPost;
    SwipeRefreshLayout swipeRefreshLayout;
    UserList listViewPersonAdapter;
    ListView listView_MainPageJafar;
    List<tb_Jafar> lstPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jafar_activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_jafar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);


        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        if (!(Build.VERSION.SDK_INT < 23)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
        }

        listView_MainPageJafar = (ListView) findViewById(R.id.listView_MainPageJafar);
        GetDataFromDB_Set();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        GetDataFromDB_Set();
                        Toast.makeText(MainActivityJafar.this, "اطلاعات بروزرسانی شد", Toast.LENGTH_SHORT).show();
                    }
                }, 3000);


            }
        });


    }

    private void GetDataFromDB_Set() {
        tb_JafarDataSource tbJafarDataSource = new tb_JafarDataSource(MainActivityJafar.this);
        tbJafarDataSource.Open();
        lstPI = tbJafarDataSource.GetList();
        tbJafarDataSource.Close();
        listViewPersonAdapter = new UserList(MainActivityJafar.this, lstPI, R.layout.customelistview, clickInterface);
        listView_MainPageJafar.setAdapter(listViewPersonAdapter);
    }

    UserList.imagetxtInterFace clickInterface = new UserList.imagetxtInterFace() {
        @Override
        public void SelectedItemCallback(tb_Jafar data) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityJafar.this);
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

            BitmapFactory.Options bitmapoption = new BitmapFactory.Options();
            bitmapoption.inJustDecodeBounds = false;


            //Bitmap.createScaledBitmap(, imageWidth, imageHeight, false)


            //Picasso.get().load(Uri.parse("file://"+ data.UrlImg)).into(imgFactorShow);


            Bitmap neadsf = BitmapFactory.decodeFile(data.UrlImg, bitmapoption);
            imgFactorShow.setImageBitmap(neadsf);


            builder.setView(linearLayoutShow);
            alertDialogJafarShowPost = builder.create();
            alertDialogJafarShowPost.show();

        }

        @Override
        public void ImageFactorCallBack(String imgUri) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityJafar.this);
            LinearLayout linearLayoutShow = (LinearLayout) getLayoutInflater().inflate(R.layout.show_post_image, null, false);

            final ImageView imvPhotoFactorShowLarg = (ImageView) linearLayoutShow.findViewById(R.id.imvPhotoFactorShowLarg);
            LinearLayout linerImageFac = (LinearLayout) linearLayoutShow.findViewById(R.id.linerImageFac);
            LinearLayout linerPost = (LinearLayout) linearLayoutShow.findViewById(R.id.linerPost);

            linerImageFac.setVisibility(View.VISIBLE);
            linerPost.setVisibility(View.GONE);

            BitmapFactory.Options bitmapoption = new BitmapFactory.Options();
            bitmapoption.inJustDecodeBounds = false;


            Bitmap neadsf = BitmapFactory.decodeFile(imgUri, bitmapoption);

            imvPhotoFactorShowLarg.setImageBitmap(neadsf);
            //     imvPhotoFactorShowLarg.setImageBitmap(BitmapFactory.decodeFile(imgUri));


            builder.setView(linearLayoutShow);
            alertDialogJafarShowPost = builder.create();
            alertDialogJafarShowPost.show();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_jafar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insertDate:
                Intent intent = new Intent(MainActivityJafar.this, InsertActivityJafar.class);
                startActivity(intent);
                break;
            case R.id.searchJafar:
                Intent intent2 = new Intent(MainActivityJafar.this, SearchActivityJafar.class);
                startActivity(intent2);
                break;
            case R.id.backupDataXls:
                BackUpExelData();
                break;
            case R.id.insertDataXls:
                ImportExelData();
                break;
        }
        return true;
    }

    private void BackUpExelData() {
        final Date date = new Date();

        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Backup/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getApplicationContext(), DatabaseManagement.databaseName, directory_path);
        sqliteToExcel.exportSingleTable(tb_JafarStructure.tableName, "dastan.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(String filePath) {
                Toast.makeText(MainActivityJafar.this, "اطلاعات با موفقیت در فایل اکسل ریخته شد", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void ImportExelData() {

        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Backup/dastan.xls";

        File file = new File(directory_path);
        if (!file.exists()) {
            return;
        }

        tb_JafarDataSource tbJafarDataSource = new tb_JafarDataSource(MainActivityJafar.this);
        tbJafarDataSource.Open();

        ExcelToSQLite excelToSQLite = new ExcelToSQLite(getApplicationContext(), DatabaseManagement.databaseName, false);
        // Import EXCEL FILE to SQLite
        excelToSQLite.importFromFile(directory_path, new ExcelToSQLite.ImportListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(String dbName) {
//                        Utils.showSnackBar(view, "Excel imported into " + dbName);
                Toast.makeText(MainActivityJafar.this, "کامل شد", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(MainActivityJafar.this, "با مشکل روبه رو شد!", Toast.LENGTH_SHORT).show();
                Log.i("ERER", e.getMessage());
//                        Utils.showSnackBar(view, "Error : " + e.getMessage());
            }
        });
        tbJafarDataSource.Close();

    }
}



