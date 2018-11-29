package vyst.business.repositories.main;


import java.io.File;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

interface MainRepositoryDataSource {

    Observable<Login> loginApi(String contentType, LoginJson loginJson);
    Observable<Orders> orderApi(String contentType, OrderJson orderJson);
    Observable<ItemsPojo> itemApi(String contentType, OrderJson orderJson);

    Observable<AddEditPojo> addEditApi(String contentType, AddEditJson addEditJson);

    Observable<RegisterShopkeeperPojo> registerApi(String contentType, RegisterJson registerJson);

    Observable<CommonResponsePojo> feedbackApi(String contentType,FeedbackJson feedbackJson);

    Observable<CommonResponsePojo> acceptOrderApi(String contentType,UpdateOrderJson updateOrderJson);
    Observable<CommonResponsePojo> cancelOrderApi(String contentType,UpdateOrderJson updateOrderJson);
    Observable<CommonResponsePojo> deliverOrderApi(String contentType,UpdateOrderJson updateOrderJson);

    Observable<CommonResponsePojo> itemAvailabilityApi(String contentType,ItemAvaialbilityJson itemAvaialbilityJson);

    Observable<CommonResponsePojo> saveTokenApi(String contentType,SaveTokenJson saveTokenJson);

    Observable<LocationPojo> getLocationList();
    Observable<UploadImagePojo> uploadImageApi(/*String contentType, RequestBody params,*/ MultipartBody.Part file);


    Observable<CommonResponsePojo> logOutApi(String contentType, OrderJson orderJson);
}
