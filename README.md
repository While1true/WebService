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
                
      webservice.doSoapNet(NewApi.ReplyConsultation,new RequestParams()
                .add("type", String.valueOf(type))
                .add("consultationCode", code)
                .add("replyContents", reply_edit.getText().toString())
                .add("replyUser", doctor_name.getText().toString())
                .add("token", ""))
                .subscribe(new MyObserve<String>() {
                    @Override
                    public void onNext(String value) {
                    
                    }
                    });
```
