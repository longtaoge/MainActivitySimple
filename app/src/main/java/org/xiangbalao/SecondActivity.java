package org.xiangbalao;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.xiangbalao.camera.EasyImage;
import org.xiangbalao.camera.easyphotopicker.DefaultCallback;
import org.xiangbalao.camera.easyphotopicker.EasyImageConfig;
import org.xiangbalao.modle.IdCard;
import org.xiangbalao.simplecropimage.R;
import org.xiangbalao.simplecropview.CropImage;

import java.io.File;


public class SecondActivity extends Activity {

    public static final String TAG = "MainActivity";


    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    private int targetImageView;


    private ImageView mImageView;
    private ImageView mImageView1;
    private File mFileTemp;
    Bitmap bitmap;



    private IdCard mIdCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState); // To change body of overridden
        // methods use File | Settings |
        // File Templates.
        setContentView(R.layout.activity_second);

        mImageView = (ImageView) findViewById(R.id.image);
        mImageView1= (ImageView) findViewById(R.id.image1);

        mIdCard =new IdCard();

        findViewById(R.id.gallery).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        EasyImage.openChooser(SecondActivity.this, "选择图片来源");

                        targetImageView=R.id.image;
                    }
                });

        findViewById(R.id.take_picture).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        EasyImage.openChooser(SecondActivity.this, "选择图片来源");
                        targetImageView=R.id.image1;
                    }
                });




    }


    private void startCropImage( File file ) {

        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, file.getPath());
        intent.putExtra(CropImage.SCALE, true);
        //比例
        intent.putExtra(CropImage.ASPECT_X, 3);
        intent.putExtra(CropImage.ASPECT_Y, 2);
        //裁剪比例
        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_CROP_IMAGE:
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {
                    return;
                }
                //TODO 处理裁剪后的图片
                onPhotoReturned(mFileTemp);
                break;

            case EasyImageConfig.REQ_PICK_PICTURE_FROM_DOCUMENTS:
            case EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY:
            case EasyImageConfig.REQ_SOURCE_CHOOSER:
            case EasyImageConfig.REQ_TAKE_PICTURE:
                processEasyImage(requestCode, resultCode, data);
                break;
            default:


                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processEasyImage(int requestCode, int resultCode, Intent data) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, SecondActivity.this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source) {
                //Some error handling
                Toast.makeText(SecondActivity.this, "拍照过程中遇到问题" + e.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source) {
                //Handle the image
                Toast.makeText(SecondActivity.this, "成功onImagePicked" + imageFile.toString(), Toast.LENGTH_LONG).show();
                //
                mFileTemp=imageFile;
                //剪切 图片
                startCropImage(imageFile);
                Log.i(SecondActivity.class.getSimpleName(), imageFile.toString());
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source) {
                Toast.makeText(SecondActivity.this, "拍照过程中遇到问题onCanceled", Toast.LENGTH_LONG).show();
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(SecondActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    private void onPhotoReturned(File imageFile) {
        bitmap = BitmapFactory.decodeFile(imageFile.getPath());


        switch ( targetImageView){

            case R.id.image:
                mImageView.setImageBitmap(bitmap);

                targetImageView=-1;
                mIdCard.setFg(imageFile.getAbsolutePath());
                break;

            case R.id.image1:
                mImageView1.setImageBitmap(bitmap);
                targetImageView=-1;
                mIdCard.setBg(imageFile.getAbsolutePath());
                break;
        }





        Log.d(SecondActivity.class.getSimpleName(), mFileTemp.getPath());
        Log.d(SecondActivity.class.getSimpleName(), mFileTemp.getAbsolutePath());
        Log.d(SecondActivity.class.getSimpleName(), mIdCard.toString());

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }
}
