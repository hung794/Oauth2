import axios from "axios"

const APIArticles = "http://localhost:8081/api/v1/articles"
const APIUsers = "http://localhost:8081/api/v1/users"

class ResourcesService {
    getArticles(token){
        let config = {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
        return axios.get(APIArticles, config)
    }

    getUsers(token){
        let config = {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
        return axios.get(APIUsers, config)
    }
}

export default new ResourcesService()