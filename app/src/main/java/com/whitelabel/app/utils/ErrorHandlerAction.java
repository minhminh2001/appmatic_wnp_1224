package com.whitelabel.app.utils;

import rx.functions.Action1;

public abstract class ErrorHandlerAction implements Action1<Throwable> {

        @Override
        public void call(Throwable throwable) {
        requestError(ExceptionParse.parseException(throwable));
         }
        protected abstract  void  requestError(com.whitelabel.app.model.ApiFaildException ex);
    }