package tw.noel.sung.com.huskybundle;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.lang.reflect.Field;

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
     *  建立鏈結
     * @param object
     */
    public void inject(Object object) {
        Activity activity;
        Fragment fragment;
        Class aClass = null;
        Bundle bundle = null;

        if (object instanceof Activity) {
            activity = (Activity) object;
            bundle = activity.getIntent().getExtras();
            aClass = activity.getClass();

        } else if (object instanceof Fragment) {
            fragment = (Fragment) object;
            bundle = fragment.getArguments();
            aClass = fragment.getClass();
        }


        if (aClass != null) {

            // 取得所有宣告變數
            Field[] declaredFields = aClass.getDeclaredFields();

            //逐一取得此Fragment or Activity宣告的所有變數
            for (Field field : declaredFields) {

                //如果該變數之Annotation為自定義錨點
                if (field.isAnnotationPresent(GetValue.class)) {
                    GetValue getValueAnnotation = field.getAnnotation(GetValue.class);

                    //變數名稱
                    String filedName = field.getName();
                    //設置的錨點名稱
                    String name = getValueAnnotation.name();

                    if (bundle != null) {
                        //取得所有bundle的資源
                        for (String key : bundle.keySet()) {
                            Object data = bundle.get(key);
                            //如有指定錨點名稱  || 如沒有指定則直接認物件名稱
                            if ((!name.equals("") && key.equals(name)) || key.equals(filedName)) {
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
            }
        }
    }
}
