package iran.com.jafar;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import Database.Table.tb_Jafar;

public class UserList extends BaseAdapter {

    private Context context;
    private List<tb_Jafar> lstPI;
    private int _templateResourceId;
    private imagetxtInterFace _imagetxtInterFace;
    private LayoutInflater inflater = null;

    TextView txtNameCom, txtNamePro, txtDate;
    ImageView imgListMain;
    int counter = 1;


    public UserList(Activity activity, List<tb_Jafar> listPerson, int templateResourceId, imagetxtInterFace imagetxtInterFaces) {
        context = activity;
        lstPI = listPerson;
        _templateResourceId = templateResourceId;
        _imagetxtInterFace = imagetxtInterFaces;
        inflater = LayoutInflater.from(context);
    }

    public interface imagetxtInterFace {
        public void SelectedItemCallback(tb_Jafar data);

        public void ImageFactorCallBack(String imgUri);
    }

    @Override
    public int getCount() {
        return lstPI.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = inflater.inflate(_templateResourceId, null);

        txtNameCom = (TextView) rowView.findViewById(R.id.txtNameCom);
        txtNamePro = (TextView) rowView.findViewById(R.id.txtNamePro);
        txtDate = (TextView) rowView.findViewById(R.id.txtDate);
        imgListMain = (ImageView) rowView.findViewById(R.id.imgListMain);

        txtNameCom.setText(lstPI.get(position).CompanyName);
        txtNamePro.setText(lstPI.get(position).productName);
        txtDate.setText(String.format("%s/%s/%s",
                lstPI.get(position).Year,
                lstPI.get(position).Month,
                lstPI.get(position).Days));

        imgListMain.setImageBitmap(BitmapFactory.decodeFile(lstPI.get(position).UrlImg));
        imgListMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_imagetxtInterFace != null) {
                    _imagetxtInterFace.ImageFactorCallBack(lstPI.get(position).UrlImg);
                }
            }
        });

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_imagetxtInterFace != null) {
                    _imagetxtInterFace.SelectedItemCallback(lstPI.get(position));
                }
            }
        });


        return rowView;
    }


}

