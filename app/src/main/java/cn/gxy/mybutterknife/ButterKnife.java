package cn.gxy.mybutterknife;

import android.app.Activity;

public class ButterKnife {

    public static void init(Activity activity) {
        //获取注解处理器生成类的 全类名
        String packageName = activity.getClass().getCanonicalName() + "_ViewBinding";
        //反射获取注解处理器 生成
        try {
            Class<?> aClass = Class.forName(packageName);

            IBinder iBinder = (IBinder) aClass.newInstance();
            //多态  调用子类实现方法
            iBinder.bind(activity);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
