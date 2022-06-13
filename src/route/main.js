import {createRouter, createWebHistory} from 'vue-router'

const routes = [
    {
        path: "",
        component: () => import("../components/HelloWorld.vue")
    },
    {
        path: "/Resource",
        component: () => import("../components/ResourceServer.vue")
    }

]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router