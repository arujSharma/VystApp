package vyst.business.repositories.main;

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

class RemoteMainRepository implements MainRepositoryDataSource {

    private final MainServices mainService;

    public RemoteMainRepository(MainServices mainService) {
        this.mainService = mainService;
    }


    @Override
    public Observable<Login> loginApi(String contentType, LoginJson loginJson) {
        return mainService.loginApi(contentType,loginJson);
    }

    @Override
    public Observable<Orders> orderApi(String contentType, OrderJson orderJson) {
        return mainService.orderApi(contentType,orderJson);
    }

    @Override
    public Observable<ItemsPojo> itemApi(String contentType, OrderJson orderJson) {
        return mainService.itemApi(contentType,orderJson);
    }

    @Override
    public Observable<AddEditPojo> addEditApi(String contentType, AddEditJson addEditJson) {
        return mainService.addEditApi(contentType,addEditJson);
    }

    @Override
    public Observable<RegisterShopkeeperPojo> registerApi(String contentType, RegisterJson registerJson) {
        return mainService.registerApi(contentType,registerJson);
    }

    @Override
    public Observable<CommonResponsePojo> feedbackApi(String contentType, FeedbackJson feedbackJson) {
        return mainService.feedbackApi(contentType,feedbackJson);
    }

    @Override
    public Observable<CommonResponsePojo> acceptOrderApi(String contentType, UpdateOrderJson updateOrderJson) {
        return mainService.acceptOrderApi(contentType,updateOrderJson);
    }

    @Override
    public Observable<CommonResponsePojo> cancelOrderApi(String contentType, UpdateOrderJson updateOrderJson) {
        return mainService.cancelOrderApi(contentType,updateOrderJson);
    }

    @Override
    public Observable<CommonResponsePojo> deliverOrderApi(String contentType, UpdateOrderJson updateOrderJson) {
        return mainService.deliverOrderApi(contentType,updateOrderJson);
    }

    @Override
    public Observable<CommonResponsePojo> itemAvailabilityApi(String contentType, ItemAvaialbilityJson itemAvaialbilityJson) {
        return mainService.itemAvailabilityApi(contentType,itemAvaialbilityJson);
    }

    @Override
    public Observable<CommonResponsePojo> saveTokenApi(String contentType, SaveTokenJson saveTokenJson) {
        return mainService.saveTokenApi(contentType,saveTokenJson);
    }

    @Override
    public Observable<LocationPojo> getLocationList() {
        return mainService.getLocationList();
    }

    @Override
    public Observable<UploadImagePojo> uploadImageApi(/*String contentType, RequestBody params,*/ MultipartBody.Part file) {
        return mainService.uploadImageApi(/*contentType,params,*/file);
    }

    @Override
    public Observable<CommonResponsePojo> logOutApi(String contentType, OrderJson orderJson) {
        return mainService.logOutApi(contentType,orderJson);
    }


}
