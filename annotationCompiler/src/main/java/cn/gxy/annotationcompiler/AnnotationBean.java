package cn.gxy.annotationcompiler;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;


public class AnnotationBean {
    private List<VariableElement> list;
    private List<ExecutableElement> executableElement;

    public AnnotationBean(List<VariableElement> list, List<ExecutableElement> executableElement) {
        this.list = list;
        this.executableElement = executableElement;
    }

    public List<VariableElement> getList() {
        return list;
    }

    public void setList(List<VariableElement> list) {
        this.list = list;
    }

    public List<ExecutableElement> getExecutableElement() {
        return executableElement;
    }

    public void setExecutableElement(List<ExecutableElement> executableElement) {
        this.executableElement = executableElement;
    }
}
