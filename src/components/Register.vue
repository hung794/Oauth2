<template>
  <div>
    <div class="form">
      <img
          src="http://www.androidpolice.com/wp-content/themes/ap2/ap_resize/ap_resize.php?src=http%3A%2F%2Fwww.androidpolice.com%2Fwp-content%2Fuploads%2F2015%2F10%2Fnexus2cee_Search-Thumb-150x150.png&w=150&h=150&zc=3">
      <h1>Register</h1>
      <input type="text" v-model="userInfo.username" name="username" placeholder="Username"/>
      <input type="password" v-model="userInfo.password" name="password" placeholder="Password"/>
      <button v-on:click="register()">Sign Up</button>
      <a href="/login">Have account? Login</a>
    </div>
  </div>
</template>
<script>
import AuthorizationService from "@/axios/AuthorizationService";
export default {
  name: "Register-form",
  data() {
    return {
      userInfo: {
        username: "",
        password: ""
      }
    }
  },
  methods: {
    register() {
      AuthorizationService.register(this.userInfo).then(() => {
        alert("Register success!")
        history.back()
      }).catch(() => {
        alert("User name is exist!")
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