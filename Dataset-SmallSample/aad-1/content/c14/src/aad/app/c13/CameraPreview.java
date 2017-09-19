
package aad.app.c13;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Camera preview class.
 *
 */
class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = CameraPreview.class.getSimpleName();

    private SurfaceHolder mHolder;
    public Camera camera;

    CameraPreview(Context context) {
        super(context);

        mHolder = this.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    // The surface is created, prepare the camera
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (camera != null) {
                camera.setPreviewDisplay(holder);
            }
        } catch (IOException exception) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }
    }

    // Properly destroy the camera if the surface is destroyed
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
        }
        camera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        // Get all the supported preview sizes for this camera
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

        // Iterate through and find a good size for this camera
        for (Camera.Size size : previewSizes) {

            Log.i(TAG, "surfaceChanged() Found size: " + size.width + "x" + size.height);

            if (size.width == 320)
                parameters.setPreviewSize(size.width, size.height);

        }

        camera.setParameters(parameters);
        camera.startPreview();
    }

}
