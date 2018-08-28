package com.cool.rpc.util;


import java.io.*;


/**
 * @auther Vincent
 * @wechat luxiaotao1123
 * @data 2018/8/27
 */
public final class JavaSerialize {

    /**
     *  serialize init
     */
    public static <T> byte[] serialize(T obj){
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     *  analyze serialize
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] data, Class<T> cls){
        Object obj;
        T t = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream (data);
            ObjectInputStream ois = new ObjectInputStream (bis);
            obj = ois.readObject();
            if (cls.isInstance(obj)){
                t = (T) obj;
            }

            ois.close();
            bis.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return t;

    }

    private JavaSerialize(){
    }

}
