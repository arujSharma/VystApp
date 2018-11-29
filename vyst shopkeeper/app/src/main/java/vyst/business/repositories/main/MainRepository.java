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

public class MainRepository implements MainRepositoryDataSource {

    private final MainRepositoryDataSource remoteGitHubRepository;

    private static MainRepository INSTANCE;


    public static MainRepository getInstance(MainServices gitHubService) {
        if (INSTANCE == null)
            INSTANCE = new MainRepository(gitHubService);
        return INSTANCE;
    }

    private MainRepository(MainServices gitHubService) {
        remoteGitHubRepository = new RemoteMainRepository(gitHubService);
    }

    @Override
    public Observable<Login> loginApi(String contentType, LoginJson loginJson) {
        return remoteGitHubRepository.loginApi(contentType,loginJson);
    }

    @Override
    public Observable<Orders> orderApi(String contentType, OrderJson orderJson) {
        return remoteGitHubRepository.orderApi(contentType,orderJson);
    }

    @Override
    public Observable<ItemsPojo> itemApi(String contentType, OrderJson orderJson) {
        return remoteGitHubRepository.itemApi(contentType,orderJson);
    }

    @Override
    public Observable<AddEditPojo> addEditApi(String contentType, AddEditJson addEditJson) {
        return remoteGitHubRepository.addEditApi(contentType,addEditJson);
    }

    @Override
    public Observable<RegisterShopkeeperPojo> registerApi(String contentType, RegisterJson registerJson) {
        return remoteGitHubRepository.registerApi(contentType,registerJson);
    }

    @Override
    public Observable<CommonResponsePojo> feedbackApi(String contentType, FeedbackJson feedbackJson) {
        return remoteGitHubRepository.feedbackApi(contentType,feedbackJson);
    }

    @Override
    public Observable<CommonResponsePojo> acceptOrderApi(String contentType, UpdateOrderJson updateOrderJson) {
        return remoteGitHubRepository.acceptOrderApi(contentType,updateOrderJson);
    }

    @Override
    public Observable<CommonResponsePojo> cancelOrderApi(String contentType, UpdateOrderJson updateOrderJson) {
        return remoteGitHubRepository.cancelOrderApi(contentType,updateOrderJson);
    }

    @Override
    public Observable<CommonResponsePojo> deliverOrderApi(String contentType, UpdateOrderJson updateOrderJson) {
        return remoteGitHubRepository.deliverOrderApi(contentType,updateOrderJson);
    }

    @Override
    public Observable<CommonResponsePojo> itemAvailabilityApi(String contentType, ItemAvaialbilityJson itemAvaialbilityJson) {
        return remoteGitHubRepository.itemAvailabilityApi(contentType,itemAvaialbilityJson);
    }

    @Override
    public Observable<CommonResponsePojo> saveTokenApi(String contentType, SaveTokenJson saveTokenJson) {
        return remoteGitHubRepository.saveTokenApi(contentType,saveTokenJson);
    }

    @Override
    public Observable<UploadImagePojo> uploadImageApi(/*String contentType, RequestBody params,*/ MultipartBody.Part file) {
        return remoteGitHubRepository.uploadImageApi(/*contentType,params,*/file);
    }

    @Override
    public Observable<CommonResponsePojo> logOutApi(String contentType, OrderJson orderJson) {
        return remoteGitHubRepository.logOutApi(contentType,orderJson);
    }

    @Override
    public Observable<LocationPojo> getLocationList() {
        return remoteGitHubRepository.getLocationList();
    }


}
