package cn.gxy.annotationcompiler;


import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

import cn.gxy.annotations.BindView;

/**
 * 这个类就是APT
 * 实现一个方法 重写三个方法
 */
@AutoService(Processor.class)
public class AnnotationCompiler extends AbstractProcessor {
    //打印日志
    private Log log;
    //代码写入的文件对象
    private Filer filer;

    /**
     * 初始化操作
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        log = new Log(processingEnvironment.getMessager());
        filer = processingEnvironment.getFiler();
    }

    /**
     * 添加需要处理的注解
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(BindView.class.getCanonicalName());
        return set;
    }

    /**
     * 设置支持的JDK版本 一般设置最新版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 制造者
     *
     * @param set              使用注解的元素集合（元素：TypeElement typeElement;//类  ExecutableElement executableElement;//方法 VariableElement variableElement;//变量）
     * @param roundEnvironment 完整元素
     * @return 返回true表示这个注解处理器不需要 后面的注解处理器处理，返回false表示可能需要 后面的注解处理器处理
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //核心逻辑代码方法
        setAnnotation(roundEnvironment);

        return false;
    }

    /**
     * 生成代码 的核心逻辑
     *
     * @param roundEnvironment
     */
    private void setAnnotation(RoundEnvironment roundEnvironment) {
        Map<String, List<VariableElement>> map = new HashMap<>();
        //获取使用BindView注解的元素集合
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindView.class);

        //元素分类保存到map
        for (Element element : elementsAnnotatedWith) {
            //获取元素所在类的类名
            VariableElement variableElement = (VariableElement) element;
            String activityName = variableElement.getEnclosingElement().getSimpleName().toString();

            //获取类里面的注解元素集合
            List<VariableElement> list = map.get(activityName);

            if (list == null) {
                list = new ArrayList<>();
                map.put(activityName, list);
            }

            list.add(variableElement);
        }
        //------------------代码执行到这元素分类结束------------

        //写文件
        if (map.size() > 0) {
            //需要对每个activity单独写一个文件
            Iterator<String> iterator = map.keySet().iterator();

            //写对象
            Writer writer = null;

            while (iterator.hasNext()) {
                //获取类名
                String activityName = iterator.next();

                //获取类名对应 元素集合
                List<VariableElement> list = map.get(activityName);

                //获取元素的包名
                //获取元数包裹成-->类元素
                TypeElement typeElement = (TypeElement) list.get(0).getEnclosingElement();
                String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).toString();

                //创建文件
                try {
                    JavaFileObject sourceFile = filer.createSourceFile(packageName + "." + activityName + "_ViewBinding");
                    //获取写对象
                    writer = sourceFile.openWriter();

//                    package cn.gxy.mybutterknife;
                    writer.write("package " + packageName + ";\n");

//                    import cn.gxy.mybutterknife.IBinder;
                    writer.write("import " + packageName + ".IBinder;\n");
//                    public class MainActivity_ViewBinding implements IBinder<cn.gxy.mybutterknife.MainActivity>{
                    writer.write("public class " + activityName + "_ViewBinding implements IBinder<" + packageName + "." + activityName + ">{\n");
//                        @Override
                    writer.write("@Override\n");
//                        public void bind(cn.gxy.mybutterknife.MainActivity target){
                    //public void bind(final cn.gxy.mybutterknife.MainActivity target){
                    writer.write("public void bind(" + packageName + "." + activityName + " target){\n");
//                            target.tv_text=(android.widget.TextView)target.findViewById(2131165325);

                    //这段代码有几个地方是变的需要获取变动的数据才可以写 ---》tv_text  TextView  2131165325
                    for (VariableElement element : list) {
                        //获取元素名称 --->tv_text
                        String name = element.getSimpleName().toString();

                        //获取元素类型
                        TypeMirror typeMirror = element.asType();

                        //获取元素注解
                        BindView annotation = element.getAnnotation(BindView.class);
                        //通过注解获取id
                        int id = annotation.value();

                        //获取变的数据就可以写了
                        writer.write("target." + name + "=(" + typeMirror + ")target.findViewById(" + id + ");\n");

                    }


                    //  }}
                    writer.write("}\n}");

                    //代码执行到这文件就写完了

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }


        }
    }


}









