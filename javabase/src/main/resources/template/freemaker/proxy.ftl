package ${package};
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
/**
* @Author: Jdragon
* @email: 1061917196@qq.com
* @Description: jdk动态代理实质
*/
public class ${className} extends Proxy implements ${interface} {
    public ${className}(InvocationHandler h) {
        super(h);
    }
<#list 0..(methodList!?size-1) as i>
    @Override
    public final ${methodList[i].retType} ${methodList[i].methodName}(
        <#list methodList[i].paramList as param>
                ${param} var${param_index}<#if param_has_next>,</#if>
        </#list>) {
        try {
            <#if (methodList[i].retType!="void")>return (${methodList[i].retType})</#if>
            <#if (methodList[i].paramList?size==0)>
            super.h.invoke(this, m${i}, (Object[])null);
            <#else>
            super.h.invoke(this, m${i}, new Object[]{
                <#list 0..(methodList[i].paramList!?size-1) as k>var${k}
                    <#if k_has_next>,</#if>
                </#list>});
            </#if>
        } catch (RuntimeException | Error e) {
            throw e;
        }catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }
</#list>
<#list 0..(methodList!?size-1) as i>
    private static Method m${i};
</#list>
    static{
        try{
            <#list 0..(methodList!?size-1) as i>
                m${i} = Class.forName("${methodList[i].className}").getMethod("${methodList[i].methodName}"
                <#list methodList[i].paramList as param>
                    ,Class.forName("${param}")
                </#list>);
            </#list>
        }  catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
