package aad.app.c20;

import android.app.Application;
import android.provider.Settings.Secure;

import org.acra.ACRA;
import org.acra.ErrorReporter;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

//@ReportsCrashes(
//        formKey = "",
//        formUri = "https://yoursite.com/submitBug",
//        customReportContent = {
//        ReportField.APP_VERSION_NAME,
//        ReportField.ANDROID_VERSION,
//        ReportField.PHONE_MODEL,
//        ReportField.STACK_TRACE,
//        ReportField.CRASH_CONFIGURATION,
//        ReportField.CUSTOM_DATA },
//        mode = ReportingInteractionMode.TOAST,
//        resToastText = R.string.crash_toast_text
//        )

//@ReportsCrashes(
//    formKey = "",
//    mailTo = "xone@uw.edu",
//    mode = ReportingInteractionMode.TOAST,
//    resToastText = R.string.crash_toast_text)

@ReportsCrashes(
    formKey = "dG5LOGp4dGctak5Qa3V4LTlPSHZTSFE6MQ", 
    mode = ReportingInteractionMode.TOAST, 
    forceCloseDialogAfterToast = false, 
    resToastText = R.string.crash_toast_text)

public class C20Application extends Application {

    @Override
    public void onCreate() {

        String androidID = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);

        ACRA.init(this);
        ErrorReporter.getInstance().putCustomData("androidID", androidID);
        super.onCreate();
    }
}
