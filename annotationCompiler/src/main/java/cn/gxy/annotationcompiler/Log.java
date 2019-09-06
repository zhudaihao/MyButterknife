package cn.gxy.annotationcompiler;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * 打印注解日志工具类
 */
public class Log {
    private Messager messager;

    public Log(Messager messager) {
        this.messager = messager;
    }


    public static Log newLog(Messager messager) {

        return new Log(messager);
    }


    public void i(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE,msg);
    }

}
