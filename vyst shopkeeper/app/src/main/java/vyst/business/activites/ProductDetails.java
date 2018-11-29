package vyst.business.activites;

import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import vyst.business.R;
import vyst.business.app.App;
import vyst.business.helper.CommonListeners;
import vyst.business.helper.DialogPopUps;
import vyst.business.pojo.AddEditJson;
import vyst.business.pojo.CommonResponsePojo;
import vyst.business.pojo.OrderJson;
import vyst.business.pojo.UploadImagePojo;
import vyst.business.pojo.addedit.AddEditPojo;
import vyst.business.repositories.main.MainRepository;
import vyst.business.util.CommonUtils;
import vyst.business.util.SharedPrefUtil;

import static okhttp3.MediaType.parse;

public class ProductDetails extends BaseActivity implements View.OnClickListener {

    TextView txt_add_product;
    EditText et_name,et_description,et_mrp,et_discount;

    TextView tv_discount_type;
    ImageView img_back,img_main,img_camera;
    String status = "add";

    private Disposable disposable;
    private MainRepository mainRepository;

    String description="",discount="",item_id="-1",item_name="",price="",type="",image="";
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        if (getIntent().hasExtra("status")) {
            status = getIntent().getStringExtra("status");
        }

        init();
    }

    private void init() {

        txt_add_product = findViewById(R.id.txt_add_product);
        txt_add_product.setOnClickListener(this);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        img_main = findViewById(R.id.img_main);
        img_camera = findViewById(R.id.img_camera);
        img_camera.setOnClickListener(this);


        et_name = findViewById(R.id.et_name);
        et_description = findViewById(R.id.et_description);
        et_mrp = findViewById(R.id.et_mrp);

        et_discount = findViewById(R.id.et_discount);
        tv_discount_type = findViewById(R.id.tv_discount_type);
        tv_discount_type.setOnClickListener(this);

        et_discount.setFocusableInTouchMode(false);
        et_discount.setFocusable(false);





        if (status.equalsIgnoreCase("add"))
            txt_add_product.setText("Add Product");
        else {
            txt_add_product.setText("Edit Product");

            if (getIntent().hasExtra("description")) {
                description = getIntent().getStringExtra("description");
                et_description.setText(description);
            }

            if (getIntent().hasExtra("discount")) {
                discount = getIntent().getStringExtra("discount");
                et_discount.setText(discount);
            }

            if (getIntent().hasExtra("item_id")) {
                item_id = getIntent().getStringExtra("item_id");

            }

            if (getIntent().hasExtra("item_name")) {
                item_name = getIntent().getStringExtra("item_name");
                et_name.setText(item_name);

            }

            if (getIntent().hasExtra("price")) {
                price = getIntent().getStringExtra("price");
                et_mrp.setText(price);

            }

            if (getIntent().hasExtra("type")) {
                type = getIntent().getStringExtra("type");
                tv_discount_type.setText(type);


                if(type.equalsIgnoreCase("")){
                    et_discount.setFocusableInTouchMode(false);
                    et_discount.setFocusable(false);

                }else{
                    et_discount.setFocusableInTouchMode(true);
                    et_discount.setFocusable(true);

                }

            }
            if (getIntent().hasExtra("image")) {
                image= getIntent().getStringExtra("image");

                if(!image.isEmpty())
                Picasso.with(ProductDetails.this)
                        .load("http://vyst.co.in/get/"+image)
                        //  .transform(new CircleTransform())
                        //  .placeholder(R.drawable.default_user_pic) //this is optional the image to display while the url image is downloading
                        //  .error(R.drawable.default_user_pic)         //this is also optional if some error has occurred in downloading the image this image would be displayed
                        .into(img_main);
            }



        }


    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.txt_add_product:

                String name = et_name.getText().toString().trim();
                String desc = et_description.getText().toString().trim();
                String mrp = et_mrp.getText().toString().trim();
                String disc_type = tv_discount_type.getText().toString().trim();
                String discount = et_discount.getText().toString().trim();

                if(name.equalsIgnoreCase("")){
                    CommonUtils.showSnackbar(ProductDetails.this,"Please enter product name");
                }
                else   if(desc.equalsIgnoreCase("")){
                    CommonUtils.showSnackbar(ProductDetails.this,"Please enter description");
                }
                else   if(mrp.equalsIgnoreCase("")){
                    CommonUtils.showSnackbar(ProductDetails.this,"Please enter price");
                }

                else if (CommonUtils.isConnectedToInternet(ProductDetails.this)) {

                   // addEditApi(name,desc,mrp,disc_type,discount);


                    if(photoFile==null){
                        CommonUtils.showSnackbar(ProductDetails.this,"Please upload image");
                    }else{
                        addImageApi(name,desc,mrp,disc_type,discount);
                    }


                } else {
                    CommonUtils.showSnackbar(ProductDetails.this, getString(R.string.please_check_your_internet_connection));

                }




                break;


            case R.id.tv_discount_type:

                DialogPopUps.discountTypesPopup(ProductDetails.this, new CommonListeners.DiscountType() {
                    @Override
                    public void flat() {
                        tv_discount_type.setText("Flat");
                        et_discount.setFocusableInTouchMode(true);
                        et_discount.setFocusable(true);
                    }

                    @Override
                    public void percentage() {
                        tv_discount_type.setText("Percentage");
                        et_discount.setFocusableInTouchMode(true);
                        et_discount.setFocusable(true);

                    }

                    @Override
                    public void noDiscount() {
                        tv_discount_type.setText("");
                        et_discount.setText("");
                        et_discount.setFocusableInTouchMode(false);
                        et_discount.setFocusable(false);
                    }

                    @Override
                    public void cancel() {

                    }
                });

                break;


            case R.id.img_camera:

                DialogPopUps.openCamera(ProductDetails.this, new CommonListeners.AlertCallBackWithButtonsInterface() {
                    @Override
                    public void positiveClick() {


                        RxImagePicker.with(ProductDetails.this).requestImage(Sources.GALLERY).subscribe(new Consumer<Uri>() {
                            @Override
                            public void accept(@NonNull Uri uri) throws Exception {
                                //Get image by uri using one of image loading libraries. I use Glide in sample app.
                                Log.d("uri", "uri " + uri);


                                UCrop.Options options = new UCrop.Options();
                                options.setToolbarColor(ContextCompat.getColor(ProductDetails.this, R.color.colorPrimary));
                                options.setStatusBarColor(ContextCompat.getColor(ProductDetails.this, R.color.colorPrimary));


                                UCrop.of(uri, Uri.fromFile(new File(ProductDetails.this.getCacheDir(), "Vyst_" + System.currentTimeMillis()+".jpeg" )))
                                        .withAspectRatio(1, 1)
                                        .withMaxResultSize(200, 200)
                                        .withOptions(options)
                                        .start(ProductDetails.this);


                            }
                        });
                    }

                    @Override
                    public void neutralClick() {

                    }

                    @Override
                    public void negativeClick() {


                        RxImagePicker.with(ProductDetails.this).requestImage(Sources.CAMERA).subscribe(new Consumer<Uri>() {
                            @Override
                            public void accept(@NonNull Uri uri) throws Exception {

                                Log.d("uri", "uri " + uri);


                                UCrop.Options options = new UCrop.Options();
                                options.setToolbarColor(ContextCompat.getColor(ProductDetails.this, R.color.colorPrimary));
                                options.setStatusBarColor(ContextCompat.getColor(ProductDetails.this, R.color.colorPrimary));


                                UCrop.of(uri, Uri.fromFile(new File(ProductDetails.this.getCacheDir(), "Vyst_" + System.currentTimeMillis()+".jpeg")))
                                        .withAspectRatio(1, 1)
                                        .withMaxResultSize(200, 200)
                                        .withOptions(options)
                                        .start(ProductDetails.this);

                                // Log.d("uri","uri "+getRealPathFromURI(getActivity(),uri));
                            }
                        });


                    }
                });


                break;


            case R.id.img_back:

                finish();

                break;


        }
    }



    private void addEditApi(String name, String desc, String mrp, String disc_type, String discount, String image_file) {

        AddEditJson addEditJson = new AddEditJson();

        addEditJson.setToken(SharedPrefUtil.getAccessToken(ProductDetails.this));
        addEditJson.setUser_id(SharedPrefUtil.getUserId(ProductDetails.this));
        addEditJson.setType(disc_type);
        addEditJson.setAvailability(true);
        addEditJson.setDescription(desc);
        addEditJson.setDiscount(discount);
        addEditJson.setDiscount_type(disc_type);
        addEditJson.setImage_name(image_file);
        addEditJson.setItem_id(item_id);
        addEditJson.setItem_name(name);
        addEditJson.setPrice(mrp);

        CommonUtils.showProgressDialog(ProductDetails.this,getResources().getString(R.string.please_wait));



        mainRepository = App.getApp().getMainRepository();
        mainRepository.addEditApi("application/json",addEditJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddEditPojo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;

                    }

                    @Override
                    public void onNext(AddEditPojo value) {

                       CommonUtils.hideDialog();

                        if(value.getMessage().equalsIgnoreCase("Successfully inserted")){
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:

                                            finish();

                                            break;

                               /* case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    dialog.dismiss();
                                    break;*/
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetails.this);
                            builder.setMessage("Item has been successfully updated").
                                    setPositiveButton("OK", dialogClickListener).
                                    setTitle("Success").show();


                        }else{
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:

                                            dialog.dismiss();

                                            break;

                               /* case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    dialog.dismiss();
                                    break;*/
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetails.this);
                            builder.setMessage(value.getMessage()).
                                    setPositiveButton("OK", dialogClickListener).
                                    setTitle("Success").show();
                        }




                    }

                    @Override
                    public void onError(Throwable e) {

                        CommonUtils.hideDialog();

                        CommonUtils.showSnackbar(ProductDetails.this, e.getMessage());


                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private void addImageApi(final String name,final String desc,final String mrp,final String disc_type,
                             final String discount) {


        OrderJson orderJson = new OrderJson();

        orderJson.setToken(SharedPrefUtil.getAccessToken(ProductDetails.this));
        orderJson.setUser_id(SharedPrefUtil.getUserId(ProductDetails.this));

        RequestBody data = RequestBody.create(parse("text/plain"), orderJson.toString());


        CommonUtils.showProgressDialog(ProductDetails.this,getResources().getString(R.string.please_wait));

        RequestBody reqFile;
        MultipartBody.Part photo;

        reqFile = RequestBody.create(parse("image/jpeg"), photoFile);
        //  reqFile = RequestBody.create(MediaType.parse("*/*"), file);

        //Vyst_20180418_135539
        photo = MultipartBody.Part.createFormData("file", photoFile.getName(), reqFile);


        mainRepository = App.getApp().getMainRepository();
        mainRepository.uploadImageApi(/*"application/json",data,*/photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UploadImagePojo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;

                    }

                    @Override
                    public void onNext(UploadImagePojo value) {

                        CommonUtils.hideDialog();

                        addEditApi(name,desc,mrp,disc_type,discount,value.getFileName());
                    }

                    @Override
                    public void onError(Throwable e) {

                        CommonUtils.hideDialog();

                        CommonUtils.showSnackbar(ProductDetails.this, e.getMessage());


                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);



        if (resultCode == ProductDetails.this.RESULT_OK
                && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            Log.d("uri", "uri " + resultUri);

            photoFile= new File(resultUri.getPath());
            
            Picasso.with(ProductDetails.this)
                    .load(resultUri)
                  //  .transform(new CircleTransform())
                  //  .placeholder(R.drawable.default_user_pic) //this is optional the image to display while the url image is downloading
                  //  .error(R.drawable.default_user_pic)         //this is also optional if some error has occurred in downloading the image this image would be displayed
                    .into(img_main);



        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.d("uri", "error " + cropError);

        }




    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }


}
