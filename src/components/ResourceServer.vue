<template>
  <div v-if="haveTokenTrue">
    <div class="text-center mt-5"><h1>Resource Server</h1></div>
    <div>
      <button class="btn btn-primary" v-on:click="viewArticles">View Articles</button>
      <button class="btn btn-primary" v-on:click="viewUsers" style="margin-left: 30px">View Users</button>
    </div>
    <div class="text-center mt-5">
      <button class="btn btn-danger" v-on:click="logout" style="margin-left: 30px">Log Out</button>
    </div>
  </div>
</template>
<script>
import {useCookies} from "vue3-cookies";
import AuthorizationService from "@/axios/AuthorizationService";
import ResourcesService from "@/axios/ResourcesService";

export default {
  name: "ResourceServer",
  data() {
    return {
      token: "",
      dataToken: {
        token: ""
      },
      haveTokenTrue: false
    }
  },
  setup() {
    const {cookies} = useCookies();
    return {cookies};
  },
  beforeMount() {
    var parUrl = window.location.search;
    var urlParams = new URLSearchParams(parUrl);
    if (urlParams.has("token")) {
      var token = window.atob(urlParams.get('token'));
      this.cookies.set("token", token);
    }
    if (!this.cookies.isKey("token")) {
      alert("Forbidden!")
      window.location.href = "/"
    } else {
      this.dataToken.token = this.cookies.get("token")
      AuthorizationService.checkToken(this.dataToken.token, this.cookies.get("client")).then(() => {
        this.token = this.cookies.get("token")
        this.haveTokenTrue = true;
      }).catch(() => {
        alert("Forbidden!")
        window.location.href = "/"
      })
    }
  },
  methods: {
    viewArticles() {
      ResourcesService.getArticles(this.token).then(() => {
        alert("Ok!")
      }).catch(() => {
        alert("you do not have permission to access this page!")
      })
    },
    viewUsers() {
      ResourcesService.getUsers(this.token).then(() => {
        alert("Ok!")
      }).catch(() => {
        alert("you do not have permission to access this page!")
      })
    },
    logout() {
      AuthorizationService.logout(this.cookies.get("userId"))
      this.cookies.remove("token")
      this.cookies.remove("client")
      this.cookies.remove("userId")
      window.location.href = "/"
    }
  }
}
</script>
<style scoped>
</style>