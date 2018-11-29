package vyst.business.repositories.main;



import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;

import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import vyst.business.pojo.CommonResponsePojo;
import vyst.business.pojo.FeedbackJson;
import vyst.business.pojo.ItemAvaialbilityJson;
import vyst.business.pojo.SaveTokenJson;
import vyst.business.pojo.UpdateOrderJson;
import vyst.business.pojo.UploadImagePojo;
import vyst.business.pojo.addedit.AddEditPojo;
import vyst.business.pojo.AddEditJson;
import vyst.business.pojo.LoginJson;
import vyst.business.pojo.OrderJson;
import vyst.business.pojo.RegisterJson;
import vyst.business.pojo.items.ItemsPojo;
import vyst.business.pojo.location.LocationPojo;
import vyst.business.pojo.login.Login;
import vyst.business.pojo.orders.Orders;
import vyst.business.pojo.register.RegisterShopkeeperPojo;

/**
 * Created by Aakash on 06/02/2018.
 */
interface MainServices {

 /*   @GET("ticker")
    Observable<List<CurrencyList>> getCurrenyList();*/



    @POST("user/login")
    Observable<Login> loginApi(
            @Header("Content-Type") String contentType,
            @Body LoginJson loginJson);

    @POST("shopkeeper/order/list")
    Observable<Orders> orderApi(
            @Header("Content-Type") String contentType,
            @Body OrderJson orderJson);

    @POST("shopkeeper/item/list")
    Observable<ItemsPojo> itemApi(
            @Header("Content-Type") String contentType,
            @Body OrderJson orderJson);


    @POST("item/add")
    Observable<AddEditPojo> addEditApi(
            @Header("Content-Type") String contentType,
            @Body AddEditJson addEditJson);


    @POST("shopkeeper/request")
    Observable<RegisterShopkeeperPojo> registerApi(
            @Header("Content-Type") String contentType,
            @Body RegisterJson registerJson);


    @POST("order/feedback")
    Observable<CommonResponsePojo> feedbackApi(
            @Header("Content-Type") String contentType,
            @Body FeedbackJson feedbackJson);


    @POST("order/accepted")
    Observable<CommonResponsePojo> acceptOrderApi(
            @Header("Content-Type") String contentType,
            @Body UpdateOrderJson updateOrderJson);


    @POST("order/cancel")
    Observable<CommonResponsePojo> cancelOrderApi(
            @Header("Content-Type") String contentType,
            @Body UpdateOrderJson updateOrderJson);


    @POST("order/delivered")
    Observable<CommonResponsePojo> deliverOrderApi(
            @Header("Content-Type") String contentType,
            @Body UpdateOrderJson updateOrderJson);


    @POST("item/availability")
    Observable<CommonResponsePojo> itemAvailabilityApi(
            @Header("Content-Type") String contentType,
            @Body ItemAvaialbilityJson itemAvaialbilityJson);



    @Multipart
    @POST("upload")
    Observable<UploadImagePojo> uploadImageApi(
          //  @Header("Content-Type") String contentType,
         //   @Part("params") RequestBody params,
            @Part MultipartBody.Part file
            );

    @POST("location/list")
    Observable<LocationPojo> getLocationList();



    @POST("save/device/info")
    Observable<CommonResponsePojo> saveTokenApi(
            @Header("Content-Type") String contentType,
            @Body SaveTokenJson saveTokenJson);


    @POST("user/logout")
    Observable<CommonResponsePojo> logOutApi(
            @Header("Content-Type") String contentType,
            @Body OrderJson orderJson);

}
