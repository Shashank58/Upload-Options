package cybrilla.uploadoptions;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView picThumbnail;
    private VideoView video;
    private Button imageUpload, videoUpload;
    private static final int RESULT_LOAD_IMG = 1;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 2;
    private static final int PIC_CAPTURED = 3;
    private static final int REQUEST_VIDEO_CAPTURE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageUpload = (Button) findViewById(R.id.image_upload);
        videoUpload = (Button) findViewById(R.id.video_upload);
        picThumbnail = (ImageView) findViewById(R.id.pic_thumbnail);
        video = (VideoView) findViewById(R.id.video);

        imageUpload.setOnClickListener(this);
        videoUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_upload:
                new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Upload Demo")
                                .setItems(R.array.image_upload_methods, new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0){
                                            Intent intent = new Intent(android.provider
                                                    .MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(intent, PIC_CAPTURED);
                                        }else {
                                            Intent intent = new Intent();
                                            intent.setType("image/*");
                                            intent.setAction(Intent.ACTION_GET_CONTENT);
                                            startActivityForResult(Intent.createChooser(intent,
                                                    "Select Picture"), RESULT_LOAD_IMG);
                                        }
                                    }
                                }).show();
                break;

            case R.id.video_upload:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Upload Demo")
                        .setItems(R.array.video_upload_methods, new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                                    }
                                } else {
                                    Intent intent = new Intent();
                                    intent.setType("video/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(intent,
                                            "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
                                }
                            }
                        }).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case RESULT_LOAD_IMG:
                    Uri uri = data.getData();
                    video.setVisibility(View.GONE);
                    picThumbnail.setVisibility(View.VISIBLE);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap
                                (getContentResolver(), uri);
                        picThumbnail.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case REQUEST_TAKE_GALLERY_VIDEO:
                    video.setVisibility(View.VISIBLE);
                    picThumbnail.setVisibility(View.GONE);
                    Uri selectedImageUri = data.getData();
                    video.setVideoURI(selectedImageUri);
                    video.start();
                    video.seekTo(100);
                    video.pause();
                    break;

                case PIC_CAPTURED:
                    video.setVisibility(View.GONE);
                    picThumbnail.setVisibility(View.VISIBLE);
                    Bitmap bm = data.getExtras().getParcelable("data");
                    picThumbnail.setImageBitmap(bm);
                    break;

                case REQUEST_VIDEO_CAPTURE:
                    video.setVisibility(View.VISIBLE);
                    picThumbnail.setVisibility(View.GONE);
                    Uri videoUri = data.getData();
                    video.setVideoURI(videoUri);
                    video.start();
                    video.seekTo(100);
                    video.pause();
                    break;
            }
        }
    }
}
