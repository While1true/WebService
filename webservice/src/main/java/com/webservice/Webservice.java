package com.webservice;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by vange on 2017/8/30.
 */

public class Webservice {

    private static boolean showlog;
    private String url, namespace;
    private Application application;
    private Gson gson;
    private int timeout;
    private SoapStringPaser paser;

    private Webservice() {
    }

    public static class Builder {
        private boolean showlog = true;
        private String url, namespace = "http://xml.apache.org/xml-soap";
        private Application application;
        private Gson gson;
        private int timeout = 12000;
        private SoapStringPaser paser;

        public Builder setShowlog(boolean showlog) {
            this.showlog = showlog;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setNamespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public Builder setApplication(Application application) {
            this.application = application;
            return this;
        }

        public Builder setGson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public Builder setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Webservice Builder() {
            Webservice webservice = new Webservice();
            if (gson == null) {
                webservice.gson = new Gson();
            } else {
                webservice.gson = gson;
            }
            webservice.showlog = showlog;
            webservice.timeout = timeout;
            if (paser == null) {
                paser = new SoapStringPaser() {
                    @Override
                    public String getString(SoapObject soapObject) {
                        String result = soapObject.getProperty(0).toString();
                        return result;
                    }
                };
            }
            if (url == null || application == null) {
                throw new NullPointerException("url 和 application 必须不为空");
            }
            webservice.paser = paser;
            webservice.url = url;
            webservice.namespace = namespace;
            webservice.application = application;
            return webservice;

        }
    }

    /**
     * String 结果
     *
     * @param methodName
     * @param maps
     * @return
     */
    public Observable<String> doSoapNet(final String methodName, final LinkedHashMap<String, String> maps) {
        return Observable.create(new ObservableOnSubscribe<String>() {
                                     @Override
                                     public void subscribe(ObservableEmitter<String> e) throws Exception {
                                         if (!isConnected(application)) {
                                             e.onError(new Throwable("网络错误"));
                                         } else {
                                             try {
                                                 String result = dotNet(methodName, maps);
                                                 Log(result);
                                                 if (result != null) {
                                                     e.onNext(result);
                                                 } else {
                                                     e.onError(new Throwable("获取失败"));
                                                 }
                                             } catch (Exception e1) {
                                                 e1.printStackTrace();
                                                 e.onError(new Throwable(e1));
                                             }
                                         }
                                         e.onComplete();
                                     }
                                 }
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * String 结果
     *
     * @param methodName
     * @param maps
     * @return
     */
    public Observable<String> doSoapNet(final String url, final String namespace, final String methodName, final LinkedHashMap<String, String> maps) {
        return Observable.create(new ObservableOnSubscribe<String>() {
                                     @Override
                                     public void subscribe(ObservableEmitter<String> e) throws Exception {
                                         if (!isConnected(application)) {
                                             e.onError(new Throwable("网络错误"));
                                         } else {
                                             try {
                                                 String result = dotNet(url, namespace, methodName, maps);
                                                 Log(result);
                                                 if (result != null) {
                                                     e.onNext(result);
                                                 } else {
                                                     e.onError(new Throwable("获取失败"));
                                                 }
                                             } catch (Exception e1) {
                                                 e1.printStackTrace();
                                                 e.onError(new Throwable(e1));
                                             }
                                         }
                                         e.onComplete();
                                     }
                                 }
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * json 结果
     *
     * @param
     * @param methodName
     * @param maps
     * @param <T>
     * @return
     */
    public <T> Observable<T> doSoapNet(final Type type, final String methodName, final LinkedHashMap<String, String> maps) {
        return Observable.create(new ObservableOnSubscribe<T>() {
                                     @Override
                                     public void subscribe(ObservableEmitter<T> e) throws Exception {
                                         if (!isConnected(application)) {
                                             e.onError(new Throwable("网络错误"));
                                         } else {
                                             try {
                                                 String result = dotNet(methodName, maps);
                                                 Log(result);
                                                 T t = gson.fromJson(result, type);
                                                 if (t != null) {
                                                     e.onNext(t);
                                                 } else {
                                                     e.onError(new Throwable("获取失败"));
                                                 }
                                             } catch (Exception e1) {
                                                 e1.printStackTrace();
                                                 e.onError(e1);
                                             }
                                         }
                                         e.onComplete();
                                     }
                                 }
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * json 结果
     *
     * @param clazz
     * @param methodName
     * @param maps
     * @param <T>
     * @return
     */
    public <T> Observable<T> doSoapNet(final Class<T> clazz, final String methodName, final LinkedHashMap<String, String> maps) {
        return Observable.create(new ObservableOnSubscribe<T>() {
                                     @Override
                                     public void subscribe(ObservableEmitter<T> e) throws Exception {
                                         if (!isConnected(application)) {
                                             e.onError(new Throwable("网络错误"));
                                         } else {
                                             try {
                                                 String result = dotNet(methodName, maps);
                                                 Log(result);
                                                 if (result != null) {
                                                     if (clazz == String.class) {
                                                         e.onNext((T) result);
                                                     } else {
                                                         T t = gson.fromJson(result, clazz);
                                                         e.onNext(t);
                                                     }
                                                 } else {
                                                     e.onError(new Throwable("获取失败"));
                                                 }
                                             } catch (Exception e1) {
                                                 e1.printStackTrace();
                                                 e.onError(e1);
                                             }
                                         }
                                         e.onComplete();
                                     }
                                 }
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * soap网络请求
     *
     * @param methodName
     * @param maps
     * @return
     * @throws Exception
     */
    private String dotNet(String methodName, LinkedHashMap<String, String> maps) throws Exception {
        return dotNet(null, null, methodName, maps);
    }

    private String dotNet(String url, String namespace, String methodName, LinkedHashMap<String, String> maps) throws Exception {
        SoapObject soapObject = new SoapObject(namespace, methodName);
        StringBuffer urlLog = new StringBuffer();
        urlLog.append("方法：" + methodName + "\n|   参数：");
        if (maps != null) {
            for (Map.Entry<String, String> objectEntry : maps.entrySet()) {
                soapObject.addProperty(objectEntry.getKey(), objectEntry.getValue());
                urlLog.append(objectEntry.getKey() + " : " + objectEntry.getValue() + ", ");
            }
        }
        Log(urlLog.toString());
        String soapAction = namespace + "/" + methodName;// SOAP Action
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(url, timeout);
        ht.debug = false;
        envelope.setOutputSoapObject(soapObject);
        ht.call(soapAction, envelope);
        if (envelope.getResponse() != null) {
            String result = paser.getString((SoapObject) envelope.bodyIn);
            Log(result);
            return result;
        } else {
            return null;
        }
    }

    public interface SoapStringPaser {
        String getString(SoapObject soapObject);
    }


    /**
     * 是否有网络
     *
     * @param context
     * @return
     */
    private boolean isConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        if (activeInfo != null) {
            return activeInfo.isConnected();
        }
        return false;
    }


    static void Log(String message) {
        if (showlog) {
            Log.i("WebUtil", "|````````````````````````````````````````````````````");
            Log.i("WebUtil", "|");
            Log.i("WebUtil", "|   " + message);
            Log.i("WebUtil", "|");
            Log.i("WebUtil", "|____________________________________________________");
        }
    }
}
