Rxjava+ WebService封装
WebServiceUtil



```
 Webservice webservice = new Webservice.Builder().setApplication(getApplication())
                .setGson(new Gson())
                .setUrl("111")
                .setShowlog(true)
                .setNamespace("dxxxc")
                .setTimeout(10000)
                .setNamespace("112").Builder();
                
                webservice.doSoapNet(...)
```
