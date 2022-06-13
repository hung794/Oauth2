<template>
  <div v-if="isLogged">
    <div class="form">
      <img
          src="http://www.androidpolice.com/wp-content/themes/ap2/ap_resize/ap_resize.php?src=http%3A%2F%2Fwww.androidpolice.com%2Fwp-content%2Fuploads%2F2015%2F10%2Fnexus2cee_Search-Thumb-150x150.png&w=150&h=150&zc=3">
      <h2>This site wants</h2>
      <form id="form">
        <div style="display:flex;justify-content: center; align-items: center; align-self: center">
          <input type="checkbox" value="user">
          <p>View your articles</p>
        </div>
        <div style="display: flex;justify-content: center; align-items: center;">
          <input type="checkbox" value="admin">
          <p>View your user</p>
        </div>
      </form>
      <button class="button-submit" v-on:click="getToken()">Submit</button>
      <button class="button-cancel" v-on:click="logout()">Cancel</button>
    </div>
  </div>
</template>
<script>
import {useCookies} from "vue3-cookies";
import AuthorizationService from "@/axios/AuthorizationService";

export default {
  name: "Scope-form",
  setup() {
    const {cookies} = useCookies();
    return {cookies};
  },
  data() {
    return {
      authCode: "",
      isLogged: false,
      scope: []
    }
  },
  beforeMount() {
    var userId = this.cookies.get("userId");
    var client = this.cookies.get("client");
    AuthorizationService.getAuthCode(userId, client).then((response) => {
      this.authCode = response.data.data.consentForm.authorizationCode.code
      console.log(this.authCode)
      this.isLogged = true
    }).catch(error => {
      alert(error.response.data.message)
      window.location.href = "/login?client=" + window.btoa(client)
    })
  },
  methods: {
    getToken() {
      var form = document.getElementById("form")
      var inputs = form.getElementsByTagName("input")
      for (var i = 0, max = inputs.length; i < max; i += 1) {
        // Take only those inputs which are checkbox
        if (inputs[i].type === "checkbox" && inputs[i].checked) {
          this.scope.push(inputs[i].value);
        }
      }
      var userId = this.cookies.get("userId");
      var client = this.cookies.get("client");
      var data = {
        authorizationCode: this.authCode,
        clientId: client.clientId,
        clientSecret: client.clientSecret,
        rootUrl: client.rootUrl,
        scope: this.scope
      }
      AuthorizationService.getToken(userId, data).then(response => {
        var token = response.data.data.data.Credential.accessToken;
        var encode = window.btoa(token);
        alert("Success!")
        window.location.href = "http://localhost:8080/resource?token=" + encode;
      }).catch(() => {
        alert("An error occurred, please login again!")
        window.location.href = "http://localhost:8080"
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
  border: 1px solid #D9D9D9;
  outline: none;
  display: block;
}

.button-submit {
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

.button-cancel {
  border-radius: 100px;
  border: none;
  background: red;
  width: 50%;
  padding: 10px;
  color: #FFFFFF;
  display: block;
  margin: 0 auto 10px auto;
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