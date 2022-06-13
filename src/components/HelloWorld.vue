<template>
  <img alt="Vue logo" src="../assets/logo.png">
  <div class="hello">
    <h1>Welcome to demo Oauth2</h1>
    <p>Here is a simple demo of the Oauth2-like authorization process</p>
    <button class="btn btn-primary" v-on:click="sendClientInfo()">Login</button>
  </div>
</template>
<script>
import AuthorizationService from "@/axios/AuthorizationService";
import {useCookies} from "vue3-cookies";

export default {
  name: 'HelloWorld',
  props: {
    msg: String
  },
  setup() {
    const {cookies} = useCookies();
    return {cookies};
  },
  data() {
    return {
      data: {
        clientId: "demoOauth2",
        rootUrl: "localhost:8080",
        clientSecret: "hung71294"
      }
    }
  },
  methods: {
    sendClientInfo() {
      if (this.cookies.isKey("token")) {
        var token = this.cookies.get("token");
        AuthorizationService.checkToken(token, this.cookies.get("client")).then(() => {
          window.location.href = "/resource"
        })
      }
      AuthorizationService.sendClientInfo(this.data)
      this.cookies.set("client", this.data)
      const encoded = window.btoa(JSON.stringify(this.data));
      window.location.href = "https://auth-client-v11.herokuapp.com/login?client=" + encoded;
    }
  }
}
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>
