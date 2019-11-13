package com.asha.md360player4android;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;
import android.util.SparseArray;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.asha.md360player4android.ijk.MediaPlayerWrapper;
import com.asha.vrlib.MD360Director;
import com.asha.vrlib.MD360DirectorFactory;
import com.asha.vrlib.MDDirectorCamUpdate;
import com.asha.vrlib.MDVRLibrary;
import com.asha.vrlib.model.BarrelDistortionConfig;
import com.asha.vrlib.model.MDHotspotBuilder;
import com.asha.vrlib.model.MDPinchConfig;
import com.asha.vrlib.model.MDPosition;
import com.asha.vrlib.model.MDRay;
import com.asha.vrlib.model.position.MDMutablePosition;
import com.asha.vrlib.plugins.MDAbsPlugin;
import com.asha.vrlib.plugins.MDWidgetPlugin;
import com.asha.vrlib.plugins.hotspot.IMDHotspot;
import com.asha.vrlib.plugins.hotspot.MDAbsHotspot;
import com.asha.vrlib.plugins.hotspot.MDSimpleHotspot;
import com.asha.vrlib.texture.MD360BitmapTexture;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

import static android.animation.PropertyValuesHolder.ofFloat;
import static com.asha.vrlib.MDVRLibrary.DISPLAY_MODE_GLASS;
import static com.asha.vrlib.MDVRLibrary.DISPLAY_MODE_NORMAL;
import static com.asha.vrlib.MDVRLibrary.INTERACTIVE_MODE_CARDBORAD_MOTION;
import static com.asha.vrlib.MDVRLibrary.INTERACTIVE_MODE_MOTION;
import static com.asha.vrlib.MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH;
import static com.asha.vrlib.MDVRLibrary.INTERACTIVE_MODE_TOUCH;
import static com.squareup.picasso.MemoryPolicy.NO_CACHE;
import static com.squareup.picasso.MemoryPolicy.NO_STORE;

/**
 * Created by hzqiujiadi on 16/4/5.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class IjkVideoPlayerActivity extends Activity implements View.OnClickListener {


    private MediaPlayerWrapper mMediaPlayerWrapper = new MediaPlayerWrapper();

    private List<MDAbsPlugin> plugins = new LinkedList<>();

    private MDPosition logoPosition = MDMutablePosition.newInstance().setY(-8.0f).setYaw(-90.0f);

    private MDPosition[] positions = new MDPosition[]{
            MDPosition.newInstance().setZ(-8.0f).setYaw(-45.0f),
            MDPosition.newInstance().setZ(-18.0f).setYaw(15.0f).setAngleX(15),
            MDPosition.newInstance().setZ(-10.0f).setYaw(-10.0f).setAngleX(-15),
            MDPosition.newInstance().setZ(-10.0f).setYaw(30.0f).setAngleX(30),
            MDPosition.newInstance().setZ(-10.0f).setYaw(-30.0f).setAngleX(-30),
            MDPosition.newInstance().setZ(-5.0f).setYaw(30.0f).setAngleX(60),
            MDPosition.newInstance().setZ(-3.0f).setYaw(15.0f).setAngleX(-45),
            MDPosition.newInstance().setZ(-3.0f).setYaw(15.0f).setAngleX(-45).setAngleY(45),
            MDPosition.newInstance().setZ(-3.0f).setYaw(0.0f).setAngleX(90),
    };


    private MDVRLibrary mVRLibrary;
    ProgressBar progress;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_md_using_surface_view_me);
        initView();
        initData();
    }

    public void initView() {
        progress = (ProgressBar) findViewById(R.id.progress);
        view = findViewById(R.id.gl_view);


        findViewById(R.id.normal).setOnClickListener(this);
        findViewById(R.id.glass).setOnClickListener(this);
        findViewById(R.id.motion).setOnClickListener(this);
        findViewById(R.id.touch).setOnClickListener(this);
        findViewById(R.id.motion_touch).setOnClickListener(this);
        findViewById(R.id.cardboard).setOnClickListener(this);
        findViewById(R.id.cardboard_touch).setOnClickListener(this);
        findViewById(R.id.sphere).setOnClickListener(this);
        findViewById(R.id.dome_180).setOnClickListener(this);
        findViewById(R.id.dome_230).setOnClickListener(this);
        findViewById(R.id.dome_upper_180).setOnClickListener(this);
        findViewById(R.id.dome_upper_230).setOnClickListener(this);
        findViewById(R.id.sphere_horizontal).setOnClickListener(this);
        findViewById(R.id.sphere_vertical).setOnClickListener(this);
        findViewById(R.id.plane).setOnClickListener(this);
        findViewById(R.id.plane_crop).setOnClickListener(this);
        findViewById(R.id.plane_full).setOnClickListener(this);
        findViewById(R.id.multi_fish_horizontal).setOnClickListener(this);
        findViewById(R.id.multi_fish_vertical).setOnClickListener(this);
        findViewById(R.id.fish_eye_radius_vertical).setOnClickListener(this);
        findViewById(R.id.anti_enable).setOnClickListener(this);
        findViewById(R.id.anti_disable).setOnClickListener(this);
        findViewById(R.id.camera_little_planet).setOnClickListener(this);
        findViewById(R.id.camera_to_normal).setOnClickListener(this);
        findViewById(R.id.button_add_plugin).setOnClickListener(this);
        findViewById(R.id.button_remove_plugin).setOnClickListener(this);
        findViewById(R.id.button_add_hotspot_front).setOnClickListener(this);
        findViewById(R.id.play).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (mVRLibrary==null)return;
        switch (v.getId()) {
            case R.id.normal:
                mVRLibrary.switchDisplayMode(getApplicationContext(), MDVRLibrary.DISPLAY_MODE_NORMAL);
                break;
            case R.id.glass:
                mVRLibrary.switchDisplayMode(getApplicationContext(), MDVRLibrary.DISPLAY_MODE_GLASS);
                break;
            case R.id.motion:
                mVRLibrary.switchInteractiveMode(getApplicationContext(), MDVRLibrary.INTERACTIVE_MODE_MOTION);
                break;
            case R.id.touch:
                mVRLibrary.switchInteractiveMode(getApplicationContext(), MDVRLibrary.INTERACTIVE_MODE_TOUCH);
                break;
            case R.id.motion_touch:
                mVRLibrary.switchInteractiveMode(getApplicationContext(), MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH);
                break;
            case R.id.cardboard:
                mVRLibrary.switchInteractiveMode(getApplicationContext(), MDVRLibrary.INTERACTIVE_MODE_CARDBORAD_MOTION);
                break;
            case R.id.cardboard_touch:
                mVRLibrary.switchInteractiveMode(getApplicationContext(), MDVRLibrary.INTERACTIVE_MODE_CARDBORAD_MOTION_WITH_TOUCH);
                break;
            case R.id.sphere:
                mVRLibrary.switchProjectionMode(getApplicationContext(), MDVRLibrary.PROJECTION_MODE_SPHERE);
                break;
            case R.id.dome_180:
                mVRLibrary.switchProjectionMode(getApplicationContext(), MDVRLibrary.PROJECTION_MODE_DOME180);
                break;
            case R.id.dome_230:
                mVRLibrary.switchProjectionMode(getApplicationContext(), MDVRLibrary.PROJECTION_MODE_DOME230);
                break;
            case R.id.dome_upper_180:
                mVRLibrary.switchProjectionMode(getApplicationContext(), MDVRLibrary.PROJECTION_MODE_DOME180_UPPER);
                break;
            case R.id.dome_upper_230:
                mVRLibrary.switchProjectionMode(getApplicationContext(), MDVRLibrary.PROJECTION_MODE_DOME230_UPPER);
                break;
            case R.id.sphere_horizontal:
                mVRLibrary.switchProjectionMode(getApplicationContext(), MDVRLibrary.PROJECTION_MODE_STEREO_SPHERE_HORIZONTAL);
                break;
            case R.id.sphere_vertical:
                mVRLibrary.switchProjectionMode(getApplicationContext(), MDVRLibrary.PROJECTION_MODE_STEREO_SPHERE_VERTICAL);
                break;
            case R.id.plane:
                mVRLibrary.switchProjectionMode(getApplicationContext(), MDVRLibrary.PROJECTION_MODE_PLANE_FIT);
                break;
            case R.id.plane_crop:
                mVRLibrary.switchProjectionMode(getApplicationContext(), MDVRLibrary.PROJECTION_MODE_PLANE_CROP);
                break;
            case R.id.plane_full:
                mVRLibrary.switchProjectionMode(getApplicationContext(), MDVRLibrary.PROJECTION_MODE_PLANE_FULL);
                break;
            case R.id.multi_fish_horizontal:
                mVRLibrary.switchProjectionMode(getApplicationContext(), MDVRLibrary.PROJECTION_MODE_MULTI_FISH_EYE_HORIZONTAL);
                break;
            case R.id.multi_fish_vertical:
                mVRLibrary.switchProjectionMode(getApplicationContext(), MDVRLibrary.PROJECTION_MODE_MULTI_FISH_EYE_VERTICAL);
                break;
            case R.id.fish_eye_radius_vertical:
                mVRLibrary.switchProjectionMode(getApplicationContext(), CustomProjectionFactory.CUSTOM_PROJECTION_FISH_EYE_RADIUS_VERTICAL);
                break;
            case R.id.anti_enable:
                mVRLibrary.setAntiDistortionEnabled(true);
                break;
            case R.id.anti_disable:
                mVRLibrary.setAntiDistortionEnabled(false);
                break;
            case R.id.camera_little_planet:
                MDDirectorCamUpdate cameraUpdate = mVRLibrary.updateCamera();
                PropertyValuesHolder near = ofFloat("near", cameraUpdate.getNearScale(), -0.5f);
                PropertyValuesHolder eyeZ = PropertyValuesHolder.ofFloat("eyeZ", cameraUpdate.getEyeZ(), 18f);
                PropertyValuesHolder pitch = PropertyValuesHolder.ofFloat("pitch", cameraUpdate.getPitch(), 90f);
                PropertyValuesHolder yaw = PropertyValuesHolder.ofFloat("yaw", cameraUpdate.getYaw(), 90f);
                PropertyValuesHolder roll = PropertyValuesHolder.ofFloat("roll", cameraUpdate.getRoll(), 0f);
                startCameraAnimation(cameraUpdate, near, eyeZ, pitch, yaw, roll);
                break;
            case R.id.camera_to_normal:
                cameraUpdate = mVRLibrary.updateCamera();
                near = ofFloat("near", cameraUpdate.getNearScale(), 0f);
                eyeZ = PropertyValuesHolder.ofFloat("eyeZ", cameraUpdate.getEyeZ(), 0f);
                pitch = PropertyValuesHolder.ofFloat("pitch", cameraUpdate.getPitch(), 0f);
                yaw = PropertyValuesHolder.ofFloat("yaw", cameraUpdate.getYaw(), 0f);
                roll = PropertyValuesHolder.ofFloat("roll", cameraUpdate.getRoll(), 0f);
                startCameraAnimation(cameraUpdate, near, eyeZ, pitch, yaw, roll);
                break;
            case R.id.button_add_plugin:
                final int index = (int) (Math.random() * 100) % positions.length;
                MDPosition position = positions[index];
                MDHotspotBuilder builder = MDHotspotBuilder.create(mImageLoadProvider)
                        .size(4f,4f)
                        .provider(0, getApplicationContext(), android.R.drawable.star_off)
                        .provider(1, getApplicationContext(), android.R.drawable.star_on)
                        .provider(10, getApplicationContext(), android.R.drawable.checkbox_off_background)
                        .provider(11, getApplicationContext(), android.R.drawable.checkbox_on_background)
                        .listenClick(new MDVRLibrary.ITouchPickListener() {
                            @Override
                            public void onHotspotHit(IMDHotspot hitHotspot, MDRay ray) {
                                if (hitHotspot instanceof MDWidgetPlugin){
                                    MDWidgetPlugin widgetPlugin = (MDWidgetPlugin) hitHotspot;
                                    widgetPlugin.setChecked(!widgetPlugin.getChecked());
                                }
                            }
                        })
                        .title("star" + index)
                        .position(position)
                        .status(0,1)
                        .checkedStatus(10,11);

                MDWidgetPlugin plugin = new MDWidgetPlugin(builder);

                plugins.add(plugin);
                mVRLibrary.addPlugin(plugin);
                break;
            case R.id.button_remove_plugin:
                if (plugins.size() > 0){
                    MDAbsPlugin plugina = plugins.remove(plugins.size() - 1);
                    mVRLibrary.removePlugin(plugina);
                }
                break;
            case R.id.button_add_hotspot_front:
                builder = MDHotspotBuilder.create(mImageLoadProvider)
                        .size(4f,4f)
                        .provider(getApplicationContext(), R.drawable.moredoo_logo)
                        .title("front logo")
                        .tag("tag-front")
                        .position(MDPosition.newInstance().setZ(-12.0f).setY(-1.0f));
                MDAbsHotspot hotspot = new MDSimpleHotspot(builder);
                hotspot.rotateToCamera();
                plugins.add(hotspot);
                mVRLibrary.addPlugin(hotspot);
                break;
            case R.id.play:
                mMediaPlayerWrapper.start();
                break;
            case R.id.pause:
                mMediaPlayerWrapper.pause();
                break;
        }
    }

    public void initData() {
        // init VR Library
        mVRLibrary = createVRLibrary();
        mMediaPlayerWrapper.init(getApplicationContext());
        mMediaPlayerWrapper.setPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                progress.setVisibility(View.GONE);
                if (mVRLibrary != null) {
                    mVRLibrary.notifyPlayerChanged();
                }
            }
        });

        mMediaPlayerWrapper.setOnVideoSizeChangedListener(new MediaPlayerWrapper.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                mVRLibrary.onTextureResize(width, height);
            }
        });
        Uri uri = getUri();
        if (uri != null) {
            mMediaPlayerWrapper.openRemoteFile(uri.toString());
            mMediaPlayerWrapper.prepare();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mVRLibrary.onResume(this);
        mMediaPlayerWrapper.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVRLibrary.onPause(this);
        mMediaPlayerWrapper.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVRLibrary.onDestroy();
        mMediaPlayerWrapper.destroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mVRLibrary.onOrientationChanged(this);
    }


    protected Uri getUri() {
        Intent i = getIntent();
        if (i == null || i.getData() == null) {
            return null;
        }
        return i.getData();
    }

    protected MDVRLibrary createVRLibrary() {
        return MDVRLibrary.with(this)
                .displayMode(DISPLAY_MODE_NORMAL)
                .interactiveMode(INTERACTIVE_MODE_MOTION)
                .asVideo(new MDVRLibrary.IOnSurfaceReadyCallback() {
                    @Override
                    public void onSurfaceReady(Surface surface) {
                        mMediaPlayerWrapper.setSurface(surface);
                    }
                })
                .ifNotSupport(new MDVRLibrary.INotSupportCallback() {
                    @Override
                    public void onNotSupport(int mode) {
                        String tip = mode == INTERACTIVE_MODE_MOTION
                                ? "onNotSupport:MOTION" : "onNotSupport:" + String.valueOf(mode);
                        Toast.makeText(IjkVideoPlayerActivity.this, tip, Toast.LENGTH_SHORT).show();
                    }
                })
                .pinchConfig(new MDPinchConfig().setMin(1.0f).setMax(8.0f).setDefaultValue(0.1f))
                .pinchEnabled(true)
                .directorFactory(new MD360DirectorFactory() {
                    @Override
                    public MD360Director createDirector(int index) {
                        return MD360Director.builder().setPitch(90).build();
                    }
                })
                .projectionFactory(new CustomProjectionFactory())
                .barrelDistortionConfig(new BarrelDistortionConfig().setDefaultEnabled(false).setScale(0.95f))
                .build(view);
    }


    private ValueAnimator animator;
    private void startCameraAnimation(final MDDirectorCamUpdate cameraUpdate, PropertyValuesHolder... values){
        if (animator != null){
            animator.cancel();
        }

        animator = ValueAnimator.ofPropertyValuesHolder(values).setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float near = (float) animation.getAnimatedValue("near");
                float eyeZ = (float) animation.getAnimatedValue("eyeZ");
                float pitch = (float) animation.getAnimatedValue("pitch");
                float yaw = (float) animation.getAnimatedValue("yaw");
                float roll = (float) animation.getAnimatedValue("roll");
                cameraUpdate.setEyeZ(eyeZ).setNearScale(near).setPitch(pitch).setYaw(yaw).setRoll(roll);
            }
        });
        animator.start();
    }




    // load resource from android drawable and remote url.
    private MDVRLibrary.IImageLoadProvider mImageLoadProvider = new ImageLoadProvider();

    // picasso impl
    private class ImageLoadProvider implements MDVRLibrary.IImageLoadProvider{

        private SimpleArrayMap<Uri,Target> targetMap = new SimpleArrayMap<>();

        @Override
        public void onProvideBitmap(final Uri uri, final MD360BitmapTexture.Callback callback) {

            final Target target = new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    // texture
                    callback.texture(bitmap);
                    targetMap.remove(uri);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    targetMap.remove(uri);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            targetMap.put(uri, target);
            Picasso.with(getApplicationContext()).load(uri).resize(callback.getMaxTextureSize(),callback.getMaxTextureSize()).onlyScaleDown().centerInside().memoryPolicy(NO_CACHE, NO_STORE).into(target);
        }
    }
}
