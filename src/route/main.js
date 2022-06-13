import {createRouter, createWebHistory} from 'vue-router'

const routes = [
    {
        path: "/login",
        component: () => import("../components/Login.vue")
    },
    {
        path: "/register",
        component: () => import("../components/Register.vue")
    }
    ,
    {
        path: "/scope",
        component: () => import("../components/Scope.vue")
    }

]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router