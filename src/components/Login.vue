<template>
  <div>
    <div class="form">
      <img
          src="http://www.androidpolice.com/wp-content/themes/ap2/ap_resize/ap_resize.php?src=http%3A%2F%2Fwww.androidpolice.com%2Fwp-content%2Fuploads%2F2015%2F10%2Fnexus2cee_Search-Thumb-150x150.png&w=150&h=150&zc=3">
      <h1>Login</h1>
      <input type="text" v-model="userInfo.username" name="username" placeholder="Username"/>
      <input type="password" v-model="userInfo.password" name="password" placeholder="Password"/>
      <button v-on:click="login()">Sign in</button>
      <a href="/register">Create account</a>
    </div>
  </div>
</template>
<script>
import AuthorizationService from "@/axios/AuthorizationService";
import {useCookies} from "vue3-cookies";

export default {
  name: "Login-form",
  data() {
    return {
      userInfo: {
        username: "",
        password: ""
      },
      clientExist: false,
      client: ""
    }
  },
  setup() {
    const {cookies} = useCookies();
    return {cookies};
  },
  beforeMount() {
    var parUrl = window.location.search;
    var urlParams = new URLSearchParams(parUrl);
    if(urlParams.has("client")){
      this.clientExist = true;
      this.client = window.atob(urlParams.get('client'));
      this.cookies.set("client", this.client);
    }
    else {
      alert("Client not found!")
      history.back();
    }
  },
  methods: {
    login() {
      AuthorizationService.login(this.userInfo).then((response) => {
        alert("Login success!")
        this.cookies.set("userId", response.data.data.data.userId);
        window.location.href = "/scope"
      }).catch(() => {
        alert("User name or password is invalid!")
      })
    }
  }
}
</script>
<style scoped>
* {
  box-sizing: border-box;
}

body {
  background-color: #eeeeee;
}

img {
  display: block;
  width: 80px;
  margin: 30px auto;
  box-shadow: 0 5px 10px -7px #333333;
  border-radius: 50%;
}

.form {
  background-color: #ffffff;
  width: 500px;
  margin: 50px auto 10px auto;
  padding: 30px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px -3px #333;
  text-align: center;
}

input {
  border-radius: 100px;
  padding: 10px 15px;
  width: 50%;
  border: 1px solid #D9D9D9;
  outline: none;
  display: block;
  margin: 20px auto 20px auto;
}

button {
  border-radius: 100px;
  border: none;
  background: #719BE6;
  width: 50%;
  padding: 10px;
  color: #FFFFFF;
  box-shadow: 0 2px 10px -3px #719BE6;
  display: block;
  margin: 55px auto 10px auto;
  cursor: pointer;
}

a {
  text-align: center;
  margin-top: 30px;
  color: #719BE6;
  text-decoration: none;
  padding: 5px;
  display: inline-block;
}

a:hover {
  text-decoration: underline;
}
</style>