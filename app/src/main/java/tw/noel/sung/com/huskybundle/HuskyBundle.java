package tw.noel.sung.com.huskybundle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by noel on 2019/7/16.
 */
public class HuskyBundle {
    private static HuskyBundle huskyBundle;

    public static HuskyBundle getInstance() {
        synchronized (HuskyBundle.class) {
            if (huskyBundle == null) {
                huskyBundle = new HuskyBundle();
            }
            return huskyBundle;
        }
    }
    //--------------------

    /***
     *   接收端 接值 - Activity
     * @param activity
     */
    public void inject(Activity activity) {
        Bundle bundle = activity.getIntent().getExtras();
        Class aClass = activity.getClass();
        getValue(activity, aClass, bundle);
    }


    //--------------------

    /***
     *   接收端 接值 - Fragment
     * @param fragment
     */
    public void inject(Fragment fragment) {
        Bundle bundle = fragment.getArguments();
        Class aClass = fragment.getClass();
        getValue(fragment, aClass, bundle);
    }

    //---------------

    /***
     *  透過映射 取值
     * @param object
     * @param aClass
     * @param bundle
     */
    private void getValue(Object object, Class aClass, Bundle bundle) {
        if (bundle != null) {

            // 取得所有宣告變數
            Field[] declaredFields = aClass.getDeclaredFields();

            //逐一取得此Fragment or Activity宣告的所有全域變數
            for (Field field : declaredFields) {


                //如果該變數具備GetValue錨點
                if (field.isAnnotationPresent(GetValue.class)) {
                    //取得錨點
                    GetValue getValueAnnotation = field.getAnnotation(GetValue.class);

                    //變數名稱
                    String fieldName = field.getName();
                    //設置的錨點名稱
                    String name = getValueAnnotation.name();


                    //取得所有bundle的資源
                    for (String key : bundle.keySet()) {
                        Object data = bundle.get(key);
                        //如有指定錨點名稱  || 如沒有指定則直接認物件名稱
                        if ((!name.equals("") && key.equals(name)) || key.equals(fieldName)) {
                            try {
                                field.setAccessible(true);
                                field.set(object, data);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } else {
            throw new NullPointerException("HuskyBundle : Bundle is Null");
        }
    }

    //-------------

    /***
     *   傳送端 設值  Activity to Activity
     * @param currentActivity  當前Activity
     * @param targetActivity    目標Activity
     */
    public void take(Activity currentActivity, Activity targetActivity) {
        Class currentClass = currentActivity.getClass();
        Class targetClass = targetActivity.getClass();

        Intent intent = new Intent(currentActivity, targetClass);
        Bundle bundle = putValue(currentActivity, currentClass, targetClass);
        intent.putExtras(bundle);
        currentActivity.startActivity(intent);
    }


    //-------------

    /***
     *   傳送端 設值  Activity to Fragment
     * @param currentActivity  當前Activity
     * @param targetFragment    目標Fragment
     */
    public Fragment take(Activity currentActivity, Fragment targetFragment) {
        Class currentClass = currentActivity.getClass();
        Class targetClass = targetFragment.getClass();
        targetFragment.setArguments(putValue(currentActivity, currentClass, targetClass));
        return targetFragment;
    }

    //-------------

    /***
     *   傳送端 設值  Fragment to Fragment
     * @param currentFragment  當前Fragment
     * @param targetFragment    目標Fragment
     */
    public Fragment take(Fragment currentFragment, Fragment targetFragment) {
        Class currentClass = currentFragment.getClass();
        Class targetClass = targetFragment.getClass();
        targetFragment.setArguments(putValue(currentFragment, currentClass, targetClass));
        return targetFragment;
    }


    //------------------

    /***
     *  定義映射規則  設值
     * @param currentObject
     * @param currentClass
     * @param targetClass
     */
    private Bundle putValue(Object currentObject, Class currentClass, Class targetClass) {
        Bundle bundle = new Bundle();

        // 取得所有宣告變數
        Field[] declaredFields = currentClass.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);

            //如果該變數之方法具備PutValue錨點
            if (field.isAnnotationPresent(PutValue.class)) {

                //取得錨點
                PutValue putValueAnnotation = field.getAnnotation(PutValue.class);


                try {

                    //變數名稱
                    String fieldName = field.getName();
                    //取得設置的錨點之所有名稱
                    List<String> names = Arrays.asList(putValueAnnotation.target());

                    //如果錨點名稱與目標Activity名稱相同
                    if (names.contains(targetClass.getSimpleName())) {

                        //取得該變數值
                        Object object = field.get(currentObject);

                        if (object instanceof String) {
                            bundle.putString(fieldName, (String) object);
                        } else if (object instanceof CharSequence) {
                            bundle.putCharSequence(fieldName, (CharSequence) object);
                        } else if (object instanceof Integer) {
                            bundle.putInt(fieldName, (Integer) object);
                        } else if (object instanceof Double) {
                            bundle.putDouble(fieldName, (double) object);
                        } else if (object instanceof Long) {
                            bundle.putLong(fieldName, (long) object);
                        } else if (object instanceof Float) {
                            bundle.putFloat(fieldName, (float) object);
                        } else if (object instanceof Boolean) {
                            bundle.putBoolean(fieldName, (Boolean) object);
                        } else if (object instanceof Byte) {
                            bundle.putByte(fieldName, (Byte) object);
                        } else if (object instanceof byte[]) {
                            bundle.putByteArray(fieldName, (byte[]) object);
                        } else if (object instanceof Parcelable) {
                            bundle.putParcelable(fieldName, (Parcelable) object);
                        } else if (object instanceof Parcelable[]) {
                            bundle.putParcelableArray(fieldName, (Parcelable[]) object);
                        } else if (object instanceof ArrayList) {
                            bundle.putParcelableArrayList(fieldName, (ArrayList) object);
                        } else if (object instanceof Serializable) {
                            bundle.putSerializable(fieldName, (Serializable) object);

                        }
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bundle;
    }

}
