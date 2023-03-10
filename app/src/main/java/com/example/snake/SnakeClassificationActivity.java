package com.example.snake;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.snake.ml.ModelUnquant;

import org.tensorflow.lite.schema.Model;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SnakeClassificationActivity extends AppCompatActivity {
    Button selectButton,predictButton,captureButton;
    TextView result;
    ImageView imageView;
    Bitmap bitmap;
   // int imageSize = 224;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_snake_classification);
        //Permission
        getPermission();

        selectButton=findViewById(R.id.selectButtonId);
        predictButton=findViewById(R.id.predictButtonId);
        captureButton=findViewById(R.id.captureButtonId);
        imageView= findViewById(R.id.imageViewId);
        result=findViewById(R.id.resultId);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });

        //capture image
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,12);

            }
        });

        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classifyImage(bitmap);
            }
        });
        /*predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    ModelUnquant model = ModelUnquant.newInstance(SnakeClassificationActivity.this);

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

                    bitmap=Bitmap.createScaledBitmap(bitmap,224,224,true);

                    inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());

                    // Runs model inference and gets result.
                    ModelUnquant.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    result.setText(getMax(outputFeature0.getFloatArray())+"");

                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }

            }
        });*/

     /*   predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    ModelUnquant model = ModelUnquant.newInstance(SnakeClassificationActivity.this);

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

                    bitmap=Bitmap.createScaledBitmap(bitmap,224,224,true);

                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
                    byteBuffer.order(ByteOrder.nativeOrder());

                    // get 1D array of 224 * 224 pixels in image
                    int [] intValues = new int[imageSize * imageSize];
                    bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

                    // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
                    int pixel = 0;
                    for(int i = 0; i < imageSize; i++){
                        for(int j = 0; j < imageSize; j++){
                            int val = intValues[pixel++]; // RGB
                            byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                            byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                            byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                        }
                    }

                    inputFeature0.loadBuffer(byteBuffer);

                    inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());

                    // Runs model inference and gets result.
                    ModelUnquant.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    float[] confidences = outputFeature0.getFloatArray();
                    // find the index of the class with the biggest confidence.
                    int maxPos = 0;
                    float maxConfidence = 0;
                    for(int i = 0; i < confidences.length; i++){
                        if(confidences[i] > maxConfidence){
                            maxConfidence = confidences[i];
                            maxPos = i;
                        }
                    }

                    String[] classes = {"non venomous","venomous"};
                    result.setText(classes[maxPos]);

                   // result.setText(getMax(outputFeature0.getFloatArray())+"");

                    // Releases model resources if no longer used.





                    // Releases model resources if no longer used.

                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }



            }
        });*/

    }

    public void classifyImage(Bitmap image){
        try {
            ModelUnquant model = ModelUnquant.newInstance(SnakeClassificationActivity.this);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int [] intValues = new int[224 * 224];
            image.getPixels(intValues,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());
            int pixle = 0;
            for(int i = 0;i< 224;i++){
                for (int j=0; j< 224;j++){
                    int val = intValues[pixle++];//RGB
                    byteBuffer.putFloat(((val >> 16)& 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8)& 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            // result.setText(labelfish[getMax(outputFeature0.getFloatArray())]+"");

            float [] probability = outputFeature0.getFloatArray();
            int max =0;
            float maxprob = 0;
            for (int i =0; i<probability.length;i++){
                if(probability[i] > maxprob){
                    maxprob = probability[i];
                    max =i;
                }
            }

            String[] classes = {"Non Venomous","Venomous"};
            result.setText(classes[max]+""+maxprob);



            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

    }

    int getMax(float[] arr){
        int max=0;
        for(int i=0;i<arr.length;i++){
            if(arr[i]>arr[max]) max=i;
        }
        return max;
    }

    //permission for opening camera
   private void getPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(SnakeClassificationActivity.this,new String[]{Manifest.permission.CAMERA},11);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if(requestCode==11){
           if(grantResults.length>0){
               if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                   this.getPermission();;
               }
           }
       }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }




   /* public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Banana", "Orange", "Pen", "Sticky Notes"};
            result.setText(classes[maxPos]);

            String s = "";
            for(int i = 0; i < classes.length; i++){
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }
            confidence.setText(s);


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==10){
            if(data!=null)
            {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    imageView.setImageBitmap(bitmap);
                    bitmap = Bitmap.createScaledBitmap(bitmap,224,224,false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode==12){
            bitmap= (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            bitmap = Bitmap.createScaledBitmap(bitmap,224,224,false);

            /*int dimension = Math.min(bitmap.getWidth(), bitmap.getHeight());
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);
            imageView.setImageBitmap(bitmap);

            bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false);
            classifyImage(bitmap);*/
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}