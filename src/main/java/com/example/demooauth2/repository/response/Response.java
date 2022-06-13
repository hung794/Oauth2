package com.example.demooauth2.repository.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private int httpCode;
    private String message;
    private HashMap<String, Object> data;


    public static final class ResponseApiBuilder {
        private int httpCode;
        private String message;
        private HashMap<String, Object> data;

        private ResponseApiBuilder() {
        }

        public static ResponseApiBuilder aResponseApi() {
            return new ResponseApiBuilder();
        }

        public ResponseApiBuilder withHttpCode(int httpCode) {
            this.httpCode = httpCode;
            return this;
        }

        public ResponseApiBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ResponseApiBuilder withData(Object data) {
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("data", data);
            this.data = hashMap;
            return this;
        }

        public ResponseApiBuilder withConsentForm(Object data) {
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("consentForm", data);
            this.data = hashMap;
            return this;
        }

        public ResponseApiBuilder withAuthorCode(Object data) {
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("code", data);
            this.data = hashMap;
            return this;
        }

        public ResponseApiBuilder withToken(Object data) {
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("token", data);
            this.data = hashMap;
            return this;
        }

        public Response build() {
            Response response = new Response();
            response.setHttpCode(httpCode);
            response.setMessage(message);
            response.setData(data);
            return response;
        }
    }
}