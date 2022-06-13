import axios from "axios"

const API = "https://auth-server-v1.herokuapp.com/api/v1/auth"

class AuthorizationService {
    sendClientInfo(data) {
        return axios.post(API + "/client", data)
    }

    checkToken(token, client){
        return axios.post(API + "/checkToken?token=" + token, client)
    }

    logout(userId){
        return axios.post(API + "/logout?userId=" + userId);
    }
}

export default new AuthorizationService()