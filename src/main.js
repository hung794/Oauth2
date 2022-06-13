import { createApp } from 'vue'
import App from './App.vue'
import route from "./route/main"
import VueCookies from 'vue3-cookies'

createApp(App).use(route).use(VueCookies).mount('#app')
