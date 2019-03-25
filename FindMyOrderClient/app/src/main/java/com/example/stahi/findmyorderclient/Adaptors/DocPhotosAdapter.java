package com.example.stahi.findmyorderclient.Adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stahi.findmyorderclient.Models.DocumentPhotos;
import com.example.stahi.findmyorderclient.R;

import java.util.List;

public class DocPhotosAdapter extends ArrayAdapter<DocumentPhotos> {

    public DocPhotosAdapter(Context context, int resource, List<DocumentPhotos> docImg) {
        super(context, resource, docImg);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.doc_photo_list_item, parent, false);
        }

        DocumentPhotos docImg = getItem(position);

        if (docImg != null) {

            ImageView docImage = v.findViewById(R.id.doc_image);
            TextView docImageTitle = v.findViewById(R.id.doc_image_title);

            Bitmap bitmapImage = StringToBitMap(docImg.DocStringImage);

            docImage.setImageBitmap(bitmapImage);
            docImageTitle.setText(docImg.Title);

        }
        return v;
    }


    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}