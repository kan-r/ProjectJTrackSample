import axios from 'axios';
import AuthService from './AuthService';

const BASE_URL = AuthService.getBaseUrl();

class UserService {

    getUserList(){
        return axios.get(BASE_URL + '/users', AuthService.getHttpOptions());
    }

    getUser(userId){
        return axios.get(BASE_URL + `/users/${userId}`, AuthService.getHttpOptions());
    }

    addUser(user){
        user.userCrt = AuthService.getAppUser();
        return axios.post(BASE_URL + '/users', user, AuthService.getHttpOptions());
    }

    updateUser(user){
        user.userMod = AuthService.getAppUser();
        return axios.put(BASE_URL + `/users/${user.userId}`, user, AuthService.getHttpOptions());
    }

    deleteUser(userId){
        return axios.delete(BASE_URL + `/users/${userId}`, AuthService.getHttpOptions());
    }
}

export default new UserService();