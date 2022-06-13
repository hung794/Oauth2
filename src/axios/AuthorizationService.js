import axios from "axios"

const API = "https://auth-server-v1.herokuapp.com/api/v1/auth"

class AuthorizationService {
    login(data) {
        return axios.post(API + "/login", data)
    }

    register(data) {
        return axios.post(API + "/register", data)
    }

    getAuthCode(userId, client){
        return axios.post(API + "/authCode?userId=" + userId, client)
    }

    getToken(userId, data){
        return axios.post(API + "/token?userId=" + userId, data)
    }
}

export default new AuthorizationService()