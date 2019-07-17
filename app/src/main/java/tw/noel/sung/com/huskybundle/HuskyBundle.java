package tw.noel.sung.com.huskybundle;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;


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
     *   映射:  接收端 接值
     * @param activity
     */
    public void inject(Activity activity) {


        Bundle bundle = activity.getIntent().getExtras();
        Class aClass = activity.getClass();


        if (bundle != null) {

            // 取得所有宣告變數
            Field[] declaredFields = aClass.getDeclaredFields();

            //逐一取得此Fragment or Activity宣告的所有變數
            for (Field field : declaredFields) {

                //如果該變數之Annotation為自定義錨點
                if (field.isAnnotationPresent(GetValue.class)) {
                    //取得錨點
                    GetValue getValueAnnotation = field.getAnnotation(GetValue.class);

                    //變數名稱
                    String filedName = field.getName();
                    //設置的錨點名稱
                    String name = getValueAnnotation.name();


                    //取得所有bundle的資源
                    for (String key : bundle.keySet()) {
                        Object data = bundle.get(key);
                        //如有指定錨點名稱  || 如沒有指定則直接認物件名稱
                        if ((!name.equals("") && key.equals(name)) || key.equals(filedName)) {
                            try {
                                field.setAccessible(true);
                                field.set(activity, data);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }


    //--------------------

    /***
     *   映射:  接收端 接值
     * @param fragment
     */
    public void inject(Fragment fragment) {

        Bundle bundle = fragment.getArguments();
        Class aClass = fragment.getClass();

        if (bundle != null) {

            // 取得所有宣告變數
            Field[] declaredFields = aClass.getDeclaredFields();

            //逐一取得此Fragment or Activity宣告的所有變數
            for (Field field : declaredFields) {

                //如果該變數之Annotation為自定義錨點
                if (field.isAnnotationPresent(GetValue.class)) {
                    //取得錨點
                    GetValue getValueAnnotation = field.getAnnotation(GetValue.class);

                    //變數名稱
                    String filedName = field.getName();
                    //設置的錨點名稱
                    String name = getValueAnnotation.name();


                    //取得所有bundle的資源
                    for (String key : bundle.keySet()) {
                        Object data = bundle.get(key);
                        //如有指定錨點名稱  || 如沒有指定則直接認物件名稱
                        if ((!name.equals("") && key.equals(name)) || key.equals(filedName)) {
                            try {
                                field.setAccessible(true);
                                field.set(fragment, data);
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
