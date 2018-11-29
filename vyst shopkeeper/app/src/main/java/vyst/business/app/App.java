package vyst.business.app;

import android.app.Application;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import vyst.business.R;
import vyst.business.globals.GlobalConstants;
import vyst.business.repositories.main.MainRepository;
import vyst.business.repositories.main.MainServiceGenerator;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
/**
 * Created by Aakash on 06/02/2018.
 */

public class App extends Application {

    private static App app;

    private MainRepository mainRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        mainRepository = MainRepository.getInstance(MainServiceGenerator.mainServices(GlobalConstants.BASE_URL));

        Trace myTrace = FirebasePerformance.getInstance().newTrace("test_trace");
        myTrace.start();

        Paper.init(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }


    public static App getApp() {
        return app;
    }

    public MainRepository getMainRepository() {
        return mainRepository;
    }
}
